package costunitimport.costunitImport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.logger.Logger;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitFile;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.address.Address;
import costunitimport.segment.ANS;
import costunitimport.segment.ASP;
import costunitimport.segment.DFU;
import costunitimport.segment.FKT;
import costunitimport.segment.IDK;
import costunitimport.segment.KTO;
import costunitimport.segment.NAM;
import costunitimport.segment.UEM;
import costunitimport.segment.UNB;
import costunitimport.segment.VDT;
import costunitimport.segment.VKG;

public class CostUnitFileImport {
	
	private final RepositoryFactory rFactory;
	
	private final Path path;
	private final List<IDK> listIDKs;
	
	private UNB unb;
	
	public CostUnitFileImport(final Path path, final RepositoryFactory repositoryFactory) {
		this.path = Objects.requireNonNull(path);
		this.rFactory = Objects.requireNonNull(repositoryFactory);
		
		listIDKs =  new ArrayList<>();
	}
	
	public void start() {
		try (Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
			lines.forEach(line -> assign(line));
		} catch (IOException e) {
			Logger.error(e);
		}
		insertCostUnitFile();
	}

	public void assign(String line) {
		final String[] data = splitLine(line);
		
		switch (data[0]) {
			case "IDK":
				IDK idk = new IDK(data);
				listIDKs.add(idk);
			case "VDT":
				getLastIDK().setCostUnitFileVDT(new VDT(data));
			case "FKT":
				getLastIDK().setCostUnitFileFKT(new FKT(data));
			case "VKG":
				getLastIDK().getCostUnitFileVKGs().add(new VKG(data, rFactory));
			case "NAM":
				getLastIDK().setCostUnitFileNAM(new NAM(data));
			case "ANS":
				getLastIDK().getCostUnitFileNAM().getCostUnitFileANSs().add(new ANS(data, rFactory));
			case "ASP":
				getLastIDK().getCostUnitFileASPs().add(new ASP(data));
			case "UEM":
				getLastIDK().getCostUnitFileUEMs().add(new UEM(data));
			case "DFU":
				List<UEM> listUEMs = getLastIDK().getCostUnitFileUEMs();
				listUEMs.get(listUEMs.size() - 1).getCostUnitFileDFUs().add(new DFU(data));
			case "KTO":
				getLastIDK().getCostUnitFileKTOs().add(new KTO(data));
			case "UNB":
				this.unb = new UNB(data, rFactory);
			default:
				//keine Verarbeitung
		}
	}

	private IDK getLastIDK() {
		return listIDKs.get(listIDKs.size() - 1);
	}

	private String[] splitLine(String line) {
		final String SEPARATOR_SIGN = "\\+";
		
		line = line.replace("?:", ":").replace("?+", "%KOMMA%");
		line = line.replace("?,", ",").replace("?'", "'");
		String[] splitData = line.split(SEPARATOR_SIGN);
		for (String fragm : splitData) {
			fragm = fragm.replace("%KOMMA%", "+");
		}
		return splitData;
	}
	
	private void insertCostUnitFile() {
		Objects.requireNonNull(unb);
		
		CostUnitFile newFile = unb.getCostUnitFile();
		
		rFactory.getCostUnitFileRepository().save(newFile);
		
		LocalDate importFileValidityFrom = newFile.getValidityFrom();
		CareProviderMethod careProviderMethod = newFile.getCareProviderMethod();
		
		//*** Institution abschliessen, anlegen, updaten
		List<CostUnitInstitution> existingInstitutions = rFactory.getCostUnitInstitutionRepository().findLatestCostUnitInstitutionsByCareProviderMethod(careProviderMethod);
	
		//*** Institutionen werden nur auf deren Gültigkeit gefiltert
		List<IDK> filteredIDKs = filterIDKsByValidityUtil(importFileValidityFrom);
		
		closeExistingInstitutions(existingInstitutions, filteredIDKs, importFileValidityFrom);
		updateAndInsertCostUnitInstitutions(existingInstitutions, filteredIDKs, importFileValidityFrom, careProviderMethod);
		
		//*** Verknüpfungen
		
		//Alle Kasseninstitutionen (IDKs) nach Leistungserbringerschlüssel 5 suchen (Alle)
		existingInstitutions = rFactory.getCostUnitInstitutionRepository().findLatestCostUnitInstitutionsByCareProviderMethod(careProviderMethod);
		
		//IK - Kasseninstitution
		Map<Integer, CostUnitInstitution> institutionNumberToInstitut = existingInstitutions.stream()
				.collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
		
		//Zu den sonsitgen Leistungserbringern 5 - alle Abrechnungscodes beschaffen (Abrechnungscode identifiziert eine Leistungserbringerart)
		List<DTAAccountingCode> accountingCodes = rFactory.getAccountingCodeRepositoryImpl()
				.findDTAAccountingCodesByCareProviderMethod(careProviderMethod);
		
		//Abrechnungsocde - Leistungserbingerart
		Map<Integer, DTAAccountingCode> mapAccountingCodesCareProviderMethod = accountingCodes.stream().distinct()
				.collect(Collectors.toMap(DTAAccountingCode::getAccountingCode, Function.identity()));
		
		for (IDK idk : filteredIDKs) {
			List<CostUnitAssignment> assignments = idk.getCostUnitAssignment(newFile.getValidityFrom(),
					institutionNumberToInstitut, mapAccountingCodesCareProviderMethod);
			CostUnitInstitution currentInstitution = institutionNumberToInstitut
					.get(idk.getInstitutionCode());
			updateAndInsertCostUnitAssignments(currentInstitution.getId(), assignments, importFileValidityFrom);
		} // ***
	}
	
	private List<IDK> filterIDKsByValidityUtil(LocalDate importFileValidityFrom) {
		List<IDK> filteredIDKs = new ArrayList<>();
		for (IDK currentIDK : listIDKs) {
			if (currentIDK.getCostUnitFileVDT().isPresent()) {
				LocalDate validityUntil = currentIDK.getCostUnitFileVDT().get().getValidityUntil();
				// GültigkeitBis liegt in der Vergangenheit
				if (validityUntil != null && validityUntil.compareTo(importFileValidityFrom) <= 0) {
					continue;
				}
			}
			filteredIDKs.add(currentIDK);
		}
		return filteredIDKs;
	}

	/**
	 * Schließt die existierenden Institutionen aufgrund der neuen Import-Datei
	 */
	private void closeExistingInstitutions(List<CostUnitInstitution> existingInstitutions, List<IDK> listIDKs, LocalDate importFileValidityFrom) {
		List<Integer> importInstitutionNumbers = listIDKs.stream().map(IDK::getInstitutionCode).collect(Collectors.toList());
		List<CostUnitInstitution> institutionsToClose = existingInstitutions.stream().filter(o -> importInstitutionNumbers.contains(o.getInstitutionNumber())).collect(Collectors.toList());
		LocalDate validityUntil = importFileValidityFrom.minusDays(1);
		for (CostUnitInstitution kotrInstitution : institutionsToClose) {
			kotrInstitution.setValidityUntil(validityUntil);
		}
		rFactory.getCostUnitInstitutionRepository().saveAll(institutionsToClose);
	}
	
	private void updateAndInsertCostUnitInstitutions(List<CostUnitInstitution> existingInstitutions,
			List<IDK> listIDKs, LocalDate importFileValidityFrom, CareProviderMethod careProviderMethod) {
		
		Map<Integer, CostUnitInstitution> mapKotrInstitutionByInstitutionNumber = existingInstitutions.stream().collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
		
		LocalDate validityUntil = importFileValidityFrom.minusDays(1);
		for (IDK costUnitFileIDK : listIDKs) {
			CostUnitInstitution existingKotrInstitution = mapKotrInstitutionByInstitutionNumber.get(costUnitFileIDK.getInstitutionCode());
			CostUnitInstitution kotrInstitutionFromFile = costUnitFileIDK.buildCostUnitInstitution(careProviderMethod);
			if (existingKotrInstitution == null) {//Neue Institution
				if (kotrInstitutionFromFile.getValidityFrom() == null) {
					kotrInstitutionFromFile.setValidityFrom(importFileValidityFrom);
				}
//				CostUnitInstitution newInstitution = repositoryFactory.getCostUnitInstitutionRepository().save(kotrInstitutionFromFile);
				Address contactAddress = costUnitFileIDK.getCostUnitAddress();
				if (contactAddress != null) {
//					contactAddress.setODAId(newInstitution.getId()); TODO
					rFactory.getCostUnitAddressRepository().save(contactAddress);
				}
			} else {
				kotrInstitutionFromFile.setId(existingKotrInstitution.getId());
				if (kotrInstitutionFromFile.getValidityFrom() == null) {
					kotrInstitutionFromFile.setValidityFrom(importFileValidityFrom);
				}
				rFactory.getCostUnitInstitutionRepository().save(kotrInstitutionFromFile);
				Address contactAddress = costUnitFileIDK.getCostUnitAddress();
				if (contactAddress != null) {
					Address currentAddress = kotrInstitutionFromFile.getAddress();
					if (currentAddress == null) {//es gibt keine, also kann die importierte geschrieben werden
						rFactory.getCostUnitAddressRepository().save(contactAddress);
					} else {//es existiert eine Adresse, abgleich auf Änderungen
						String oldAddressString = currentAddress.getZip().getId() + "|" + currentAddress.getStreet() + "|" + currentAddress.getPostBox();
						String newAddressString = contactAddress.getZip().getId() + "|" + contactAddress.getStreet() + "|" + contactAddress.getPostBox();
						if (!oldAddressString.equals(newAddressString)) {
							currentAddress.setValidityUntil(validityUntil);
							rFactory.getCostUnitAddressRepository().save(currentAddress); //update
							rFactory.getCostUnitAddressRepository().save(contactAddress);
						}
					}
				}
			}
		}
	}
	
	private void updateAndInsertCostUnitAssignments(int kotrInstitutionId, List<CostUnitAssignment> kotrAssignmentsFile, LocalDate importFileValidityFrom) {
		List<CostUnitAssignment> kotrAssignmentsDB = rFactory.getCostUnitAssignmentRepository().findByInstitutionIdAndValidityFrom(kotrInstitutionId, importFileValidityFrom);
		if (!kotrAssignmentsDB.isEmpty() && kotrAssignmentsDB.stream().filter(assignment -> assignment.getValidityFrom().equals(importFileValidityFrom)).count() > 0) {
			//Die aktuell hinterlegten Verknüpfungen haben die gleiche GültigkeitAb wie die Datensätze die nun importiert werden sollen
			//Der Fall kann entstehen, 
		}

		List<CostUnitAssignment> kotrAssignmentsToClose = getCostUnitAssignmentsWithoutMatchSecondList(kotrAssignmentsDB, kotrAssignmentsFile);
		List<CostUnitAssignment> kotrAssignmentsToInsert = getCostUnitAssignmentsWithoutMatchSecondList(kotrAssignmentsFile, kotrAssignmentsDB);

		LocalDate validityUntil = importFileValidityFrom.minusDays(1);

		for (CostUnitAssignment kotrAssignment : kotrAssignmentsToClose) {
			if (kotrAssignment.getValidityFrom().compareTo(validityUntil) >= 0) {
				//Eventuell für Verknüpfungen mit der Veknüpfungsart 01-Verweis vom IK der Versichertenkarte zum Kostenträger kann
				//dies manchmal vorkommen, dass eine Institution sich in mehreren gleichzeit gültigen Kostenträgerdateien befindet
				if (kotrAssignment.getTypeAssignment().getId() != 1) {
					throw new IllegalArgumentException("Fehlerhafte Verknüpfungen!!! KotrInstitutionId:" + kotrInstitutionId + " kotrAssignmentId:" + kotrAssignment.getId());
				}
			} else {
				kotrAssignment.setValidityUntil(validityUntil);
			}
		}
		rFactory.getCostUnitAssignmentRepository().saveAll(kotrAssignmentsToClose); //update
		rFactory.getCostUnitAssignmentRepository().saveAll(kotrAssignmentsToInsert);

	}
	
	/**
	 * Sucht aus der 1ten Liste die Verknüpfungen heraus die sich nicht in der 2ten Liste befinden<br>
	 * 1.Liste DB, 2.Liste Import => Verknüpfungen die abgeschlossen werden müssen (Delete)<br>
	 * 1.Liste Import, 2.Liste DB => Verknüpfungen die gespeichert werden müssen (Insert)
	 * 
	 * @param kotrAssignmentsFirst
	 * @param kotrAssignmentsSecond
	 * @return Liste
	 */
	private static List<CostUnitAssignment> getCostUnitAssignmentsWithoutMatchSecondList(List<CostUnitAssignment> kotrAssignmentsFirst, List<CostUnitAssignment> kotrAssignmentsSecond) {
		if (kotrAssignmentsFirst.isEmpty()) {
			return new ArrayList<>();
		}
		List<String> listAssignmentsFileCompareStrings = kotrAssignmentsSecond.stream().map(CostUnitFileImport::getCompareString).collect(Collectors.toList());
		List<CostUnitAssignment> kotrAssignmentsWithoutMatch = new ArrayList<>();
		for (CostUnitAssignment kotrAssignment : kotrAssignmentsFirst) {
			if (!listAssignmentsFileCompareStrings.contains(getCompareString(kotrAssignment))) {
				kotrAssignmentsWithoutMatch.add(kotrAssignment);
			}
		}
		return kotrAssignmentsWithoutMatch;
	}
	
	private static String getCompareString(CostUnitAssignment assignment) {
		StringBuilder builder = new StringBuilder();
		builder.append("typeAssignment:").append(assignment.getTypeAssignment() == null ? null : assignment.getTypeAssignment().getId()).append("|");
		builder.append("institutionId:").append(assignment.getInstitutionId()).append("|");
		builder.append("institutionIdAssignment:").append(assignment.getInstitutionIdAssignment()).append("|");
		builder.append("institutionIdAccounting:").append(assignment.getInstitutionIdAccounting()).append("|");
		builder.append("typeDataSupply:").append(assignment.getTypeDataSupply() == null ? null : assignment.getTypeDataSupply().getId()).append("|");
		builder.append("typeMedium:").append(assignment.getTypeMedium() == null ? null : assignment.getTypeMedium().getId()).append("|");
		builder.append("federalStateClassificationId:").append(assignment.getFederalStateClassificationId()).append("|");
		builder.append("districtId:").append(assignment.getDistrictId()).append("|");
		builder.append("rateCode:").append(assignment.getRateCode()).append("|");
//		TODO
//		builder.append("accountingCodes:").append(
//				assignment.getAccountingCodes().isEmpty() ? null : assignment.getAccountingCodes().stream().map(DTAAccountingCode::getAccountingCode).sorted().collect(Collectors.joining(",")));
		return builder.toString();
	}
}
