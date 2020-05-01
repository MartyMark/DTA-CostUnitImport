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
import java.util.Optional;
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
	private final List<IDK> listIDKs = new ArrayList<>();
	
	private UNB unb;
	
	public CostUnitFileImport(final Path path, final RepositoryFactory repositoryFactory) {
		this.path = Objects.requireNonNull(path);
		this.rFactory = Objects.requireNonNull(repositoryFactory);
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
				IDK idk = new IDK(data, rFactory);
				listIDKs.add(idk);
				break;
			case "VDT":
				getLastIDK().setCostUnitFileVDT(new VDT(data));
				break;
			case "FKT":
				getLastIDK().setFKT(new FKT(data));
				break;
			case "VKG":
				getLastIDK().getCostUnitFileVKGs().add(new VKG(data, rFactory));
				break;
			case "NAM":
				getLastIDK().setCostUnitFileNAM(new NAM(data));
				break;
			case "ANS":
				getLastIDK().getCostUnitFileNAM().getCostUnitFileANSs().add(new ANS(data, rFactory));
				break;
			case "ASP":
				getLastIDK().getCostUnitFileASPs().add(new ASP(data));
				break;
			case "UEM":
				getLastIDK().getCostUnitFileUEMs().add(new UEM(data));
				break;
			case "DFU":
				List<UEM> listUEMs = getLastIDK().getCostUnitFileUEMs();
				listUEMs.get(listUEMs.size() - 1).getCostUnitFileDFUs().add(new DFU(data));
				break;
			case "KTO":
				getLastIDK().getCostUnitFileKTOs().add(new KTO(data));
				break;
			case "UNB":
				this.unb = new UNB(data, rFactory);
				break;
			default:
				//keine Verarbeitung
		}
	}

	private IDK getLastIDK() {
		return listIDKs.get(listIDKs.size() - 1);
	}

	private String[] splitLine(String line) {
		final String seperator = "\\+";
		
		line = line.replace("?:", ":").replace("?+", "%KOMMA%");
		line = line.replace("?,", ",").replace("?'", "'");
		return line.split(seperator);
	}
	
	private void insertCostUnitFile() {
		CostUnitFile newFile = Objects.requireNonNull(unb).getCostUnitFile();
		
		rFactory.getCostUnitFileRepository().save(newFile);
		
		CareProviderMethod careProviderMethod = newFile.getCareProviderMethod();
		
		/* Beschafft sich alle bereits vorhandenen Kasseninstitutionen */
		List<CostUnitInstitution> existingInstitutions = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionsByCareProviderMethod(careProviderMethod);
		//IK - Kasseninstitution
		Map<Integer, CostUnitInstitution> institutionNumberToInstitut = existingInstitutions.stream()
				.collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
		
		for(IDK idk : listIDKs) {
			switch(idk.getFKT().getProcessingIndicator()) {
			case "01":
				registerCostUnitInstitution(idk, careProviderMethod, newFile.getValidityFrom(), institutionNumberToInstitut);
				break;
			case "02":
				if(exist(idk, institutionNumberToInstitut)) {
					updateCostUnitInstitution(idk, careProviderMethod, newFile.getValidityFrom(), institutionNumberToInstitut);
				}else {
					registerCostUnitInstitution(idk, careProviderMethod, newFile.getValidityFrom(), institutionNumberToInstitut);
				}
				break;
			case "03":
				//Noch nicht implmentiert
				break;
			case "04":
				if(notexist(idk, institutionNumberToInstitut)) {
					registerCostUnitInstitution(idk, careProviderMethod, newFile.getValidityFrom(), institutionNumberToInstitut);
				}
			default:
				throw new IllegalArgumentException("Verarbeitungskennzeichen wird nicht unterstützt");
			}
		}
		
		
		
		//.........................................
		
		//*** Institution abschliessen, anlegen, updaten
		
		//TODO nach Kassenart trennen + Latest
	
		//*** Institutionen werden nur auf deren Gültigkeit gefiltert
		List<IDK> validityIDKs = filterIDKsByValidityUtil(newFile.getValidityFrom());
		
		//TODO Institution in neuer Datei nicht vorhanden -> muss abgeschlossen werden
		closeExsistingInstitutions(existingInstitutions, validityIDKs, newFile.getValidityFrom());
		
		updateCostUnitInstitutions(existingInstitutions, validityIDKs, newFile.getValidityFrom(), careProviderMethod);
		
		//*** Verknüpfungen
		
		//Alle Kasseninstitutionen (IDKs) nach Leistungserbringerschlüssel 5 suchen (Alle)
		existingInstitutions = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionsByCareProviderMethod(careProviderMethod);
		
		//IK - Kasseninstitution
		institutionNumberToInstitut = existingInstitutions.stream()
				.collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
		
		//Zu den sonsitgen Leistungserbringern 5 - alle Abrechnungscodes beschaffen (Abrechnungscode identifiziert eine Leistungserbringerart)
		List<DTAAccountingCode> accountingCodes = rFactory.getAccountingCodeRepositoryCustom()
				.findDTAAccountingCodesByCareProviderMethod(careProviderMethod);
		
		//Abrechnungsocde - Leistungserbingerart
		Map<Integer, DTAAccountingCode> mapAccountingCodesCareProviderMethod = accountingCodes.stream().distinct()
				.collect(Collectors.toMap(DTAAccountingCode::getAccountingCode, Function.identity()));
		
		for (IDK idk : validityIDKs) {
			List<CostUnitAssignment> assignments = idk.getCostUnitAssignment(newFile.getValidityFrom(),
					institutionNumberToInstitut, mapAccountingCodesCareProviderMethod);
			CostUnitInstitution currentInstitution = institutionNumberToInstitut
					.get(idk.getInstitutionCode());
			updateAndInsertCostUnitAssignments(currentInstitution.getId(), assignments, newFile.getValidityFrom());
		} // ***
	}
	
	/**
	 * Filtert die IDKs nach dem GültigkeitBis Datum.<br>
	 * 
	 * Liegt das in dem VDT Segment vorhandene GültigkeitBis-Datum vor dem GültigkeitAb-Datum der Kostenträgerdatei, ist die IDK/Kasseninstitution nicht mehr gültig.
	 * 
	 * @return es werden nur gültige IDKs zurückgegeben
	 */
	private List<IDK> filterIDKsByValidityUtil(LocalDate importFileValidityFrom) {
		List<IDK> validityIDKs = new ArrayList<>();
		for (IDK idk : listIDKs) {
			LocalDate validityUntil = idk.getVDT().getValidityUntil();
			// GültigkeitBis liegt in der Vergangenheit
			if (validityUntil != null && validityUntil.compareTo(importFileValidityFrom) <= 0) {
				continue;
			}
			validityIDKs.add(idk);
		}
		return validityIDKs;
	}

	/**
	 * Schließt die existierenden Institutionen aufgrund der neuen Import-Datei
	 */
	private void closeExsistingInstitutions(List<CostUnitInstitution> existingInstitutions, List<IDK> validityIDKs, LocalDate importFileValidityFrom) {
		List<Integer> importInstitutionNumbers = validityIDKs.stream().map(IDK::getInstitutionCode).collect(Collectors.toList());
		
		List<CostUnitInstitution> institutionsToClose = existingInstitutions.stream().filter(o -> importInstitutionNumbers.contains(o.getInstitutionNumber())).collect(Collectors.toList());
		
		LocalDate validityUntil = importFileValidityFrom.minusDays(1);
		for (CostUnitInstitution institution : institutionsToClose) {
			institution.setValidityUntil(validityUntil);
		}
		rFactory.getCostUnitInstitutionRepository().saveAll(institutionsToClose);
	}
	
	private void updateCostUnitInstitutions(List<CostUnitInstitution> existingInstitutions,
			List<IDK> listIDKs, LocalDate importFileValidityFrom, CareProviderMethod careProviderMethod) {
		
		/* Hier wird die IK zu der Kasse gemappt*/
		Map<Integer, CostUnitInstitution> institutionMap = existingInstitutions.stream().collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
		
		for (IDK idk : listIDKs) {
			CostUnitInstitution existingInstitution = institutionMap.get(idk.getInstitutionCode());
			CostUnitInstitution institutionFromFile = idk.buildCostUnitInstitution(careProviderMethod);
			if (existingInstitution == null) {//Neue Institution
				if (institutionFromFile.getValidityFrom() == null) {
					institutionFromFile.setValidityFrom(importFileValidityFrom);
				}
				idk.buildCostUnitAddress().ifPresent(address -> rFactory.getCostUnitAddressRepository().save(address));
				rFactory.getCostUnitInstitutionRepository().save(institutionFromFile);
			} else {
				institutionFromFile.setId(existingInstitution.getId());
				if (institutionFromFile.getValidityFrom() == null) {
					institutionFromFile.setValidityFrom(importFileValidityFrom);
				}
				rFactory.getCostUnitInstitutionRepository().save(institutionFromFile);
				Optional<Address> contactAddress = idk.buildCostUnitAddress();
				if (contactAddress.isPresent()) {
					Address currentAddress = institutionFromFile.getAddress();
					if (currentAddress == null) {//es gibt keine, also kann die importierte geschrieben werden
						rFactory.getCostUnitAddressRepository().save(contactAddress.get());
					} else {//es existiert eine Adresse, abgleich auf Änderungen
						String oldAddressString = currentAddress.getZip().getId() + "|" + currentAddress.getStreet() + "|" + currentAddress.getPostBox();
						String newAddressString = contactAddress.get().getZip().getId() + "|" + contactAddress.get().getStreet() + "|" + contactAddress.get().getPostBox();
						if (!oldAddressString.equals(newAddressString)) {
							LocalDate validityUntil = importFileValidityFrom.minusDays(1);
							currentAddress.setValidityUntil(validityUntil);
							rFactory.getCostUnitAddressRepository().save(currentAddress); //update
							rFactory.getCostUnitAddressRepository().save(contactAddress.get());
						}
					}
				}
			}
		}
	}
	
	private void updateAndInsertCostUnitAssignments(int kotrInstitutionId, List<CostUnitAssignment> assignmentsFromFile, LocalDate importFileValidityFrom) {
		List<CostUnitAssignment> exisitingAssignments = rFactory.getCostUnitAssignmentRepository().findByInstitutionIdAndValidityFrom(kotrInstitutionId, importFileValidityFrom);
		if (!exisitingAssignments.isEmpty() && exisitingAssignments.stream().filter(assignment -> assignment.getValidityFrom().equals(importFileValidityFrom)).count() > 0) {
			//Die aktuell hinterlegten Verknüpfungen haben die gleiche GültigkeitAb wie die Datensätze die nun importiert werden sollen
			//Der Fall kann entstehen, 
		}

		List<CostUnitAssignment> kotrAssignmentsToClose = getCostUnitAssignmentsWithoutMatchSecondList(exisitingAssignments, assignmentsFromFile);
		List<CostUnitAssignment> kotrAssignmentsToInsert = getCostUnitAssignmentsWithoutMatchSecondList(assignmentsFromFile, exisitingAssignments);

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
	
	
	
	
	//......................
	
	private void registerCostUnitInstitution(final IDK idk, final CareProviderMethod careProviderMethod, LocalDate importFileValidityFrom, Map<Integer, CostUnitInstitution> institutionNumberToInstitut) {
		CostUnitInstitution institutionFromFile = idk.buildCostUnitInstitution(careProviderMethod);
		if(institutionFromFile.getValidityFrom() == null) {
			institutionFromFile.setValidityFrom(importFileValidityFrom);
		}
		idk.buildCostUnitAddress().ifPresent(address -> rFactory.getCostUnitAddressRepository().save(address));
		rFactory.getCostUnitInstitutionRepository().save(institutionFromFile);
		
		List<CostUnitAssignment> assignments = idk.getCostUnitAssignmentX(importFileValidityFrom, institutionNumberToInstitut);
		updateAndInsertCostUnitAssignments(institutionFromFile.getId(), assignments, importFileValidityFrom);
	}
	
	private void updateCostUnitInstitution(IDK idk, CareProviderMethod careProviderMethod, LocalDate importFileValidityFrom, Map<Integer, CostUnitInstitution> institutionNumberToInstitut) {
		LocalDate validityUntil = importFileValidityFrom.minusDays(1);
			
		CostUnitInstitution institution = institutionNumberToInstitut.get(idk.getInstitutionCode());
		institution.setValidityUntil(validityUntil);
		rFactory.getCostUnitInstitutionRepository().save(institution);
		
		CostUnitInstitution institutionFromFile = idk.buildCostUnitInstitution(careProviderMethod);
		institutionFromFile.setId(institution.getId());
		if (institutionFromFile.getValidityFrom() == null) {
			institutionFromFile.setValidityFrom(importFileValidityFrom);
		}
		rFactory.getCostUnitInstitutionRepository().save(institutionFromFile);
		Optional<Address> contactAddress = idk.buildCostUnitAddress();
		if (contactAddress.isPresent()) {
			Address currentAddress = institutionFromFile.getAddress();
			if (currentAddress == null) {//es gibt keine, also kann die importierte geschrieben werden
				rFactory.getCostUnitAddressRepository().save(contactAddress.get());
			} else {//es existiert eine Adresse, abgleich auf Änderungen
				String oldAddressString = currentAddress.getZip().getId() + "|" + currentAddress.getStreet() + "|" + currentAddress.getPostBox();
				String newAddressString = contactAddress.get().getZip().getId() + "|" + contactAddress.get().getStreet() + "|" + contactAddress.get().getPostBox();
				if (!oldAddressString.equals(newAddressString)) {
					currentAddress.setValidityUntil(validityUntil);
					rFactory.getCostUnitAddressRepository().save(currentAddress); //update
					rFactory.getCostUnitAddressRepository().save(contactAddress.get());
				}
			}
		}
		List<CostUnitAssignment> assignments = idk.getCostUnitAssignmentX(importFileValidityFrom, institutionNumberToInstitut);
		updateAndInsertCostUnitAssignments(institutionFromFile.getId(), assignments, importFileValidityFrom);
	}
	
	private boolean exist(IDK idk, Map<Integer, CostUnitInstitution> institutionNumberToInstitut) {
		CostUnitInstitution institution = institutionNumberToInstitut.get(idk.getInstitutionCode());
		return institution != null;
	}
	
	private boolean notexist(IDK idk, Map<Integer, CostUnitInstitution> institutionNumberToInstitut) {
		CostUnitInstitution institution = institutionNumberToInstitut.get(idk.getInstitutionCode());
		return institution == null;
	}
	
	
}
