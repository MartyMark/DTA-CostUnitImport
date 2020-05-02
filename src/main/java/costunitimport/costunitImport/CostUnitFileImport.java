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
				getLastIDK().setVDT(new VDT(data));
				break;
			case "FKT":
				getLastIDK().setFKT(new FKT(data));
				break;
			case "VKG":
				getLastIDK().addVKG(new VKG(data, rFactory));
				break;
			case "NAM":
				getLastIDK().setNAM(new NAM(data));
				break;
			case "ANS":
				Optional<NAM> nam = getLastIDK().getNAM();
				nam.ifPresent(x -> x.addANS(new ANS(data, rFactory)));
				break;
			case "ASP":
				getLastIDK().addASP(new ASP(data));
				break;
			case "UEM":
				getLastIDK().addUEM(new UEM(data));
				break;
			case "DFU":
				List<UEM> listUEMs = getLastIDK().getUEMs();
				listUEMs.get(listUEMs.size() - 1).getCostUnitFileDFUs().add(new DFU(data));
				break;
			case "KTO":
				getLastIDK().addKTO(new KTO(data));
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
		
		//*** Institutionen
		
		List<IDK> filterdIDKs = filterIDKs(newFile.getValidityFrom(), newFile.getCareProviderMethod());
		
		//TODO Institution in neuer Datei nicht vorhanden -> muss abgeschlossen werden
		closeInstitutions(filterdIDKs, newFile.getValidityFrom(), newFile.getCareProviderMethod());
		
		updateInstitutions(filterdIDKs, newFile.getValidityFrom(), careProviderMethod);
		//*** Verknüpfungen
		
		

		
		/* Beschafft sich zu einem Leistungsverfahren alle gültigen (latest) Kasseninstitutionen. */
		List<CostUnitInstitution> existingInstitutions = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionsByCareProviderMethodId(careProviderMethod.getId());
		
		//IK - Kasseninstitution
		Map<Integer, CostUnitInstitution> institutionNumberToInstitut = existingInstitutions.stream()
				.collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
		
		//*** Verknüpfungen
		
		//Alle Kasseninstitutionen (IDKs) nach Leistungserbringerschlüssel 5 suchen (Alle)
		existingInstitutions = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionsByCareProviderMethodId(careProviderMethod.getId());
		
		//IK - Kasseninstitution
		institutionNumberToInstitut = existingInstitutions.stream()
				.collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
		
		//Zu den sonsitgen Leistungserbringern 5 - alle Abrechnungscodes beschaffen (Abrechnungscode identifiziert eine Leistungserbringerart)
		List<DTAAccountingCode> accountingCodes = rFactory.getAccountingCodeRepositoryCustom()
				.findDTAAccountingCodesByCareProviderMethod(careProviderMethod);
		
		//Abrechnungsocde - Leistungserbingerart
		Map<Integer, DTAAccountingCode> mapAccountingCodesCareProviderMethod = accountingCodes.stream().distinct()
				.collect(Collectors.toMap(DTAAccountingCode::getAccountingCode, Function.identity()));
		
		for (IDK idk : filterdIDKs) {
			List<CostUnitAssignment> assignments = idk.getCostUnitAssignment(newFile.getValidityFrom(),
					institutionNumberToInstitut, mapAccountingCodesCareProviderMethod);
			CostUnitInstitution currentInstitution = institutionNumberToInstitut
					.get(idk.getInstitutionCode());
			updateAndInsertCostUnitAssignments(currentInstitution.getId(), assignments, newFile.getValidityFrom());
		} // ***
	}
	
	/**
	 * Filtert die IDKs der Importdatei.<br><br>
	 * 
	 * Filterung :<br>
	 * IDK wird gefiltert wenn,<br><br>
	 * 
	 * 1. Das GülitgBis-Datum vor dem GültigAb-Datum der Importdatei liegt<br>
	 * 2. Das Verarbeitungskennzeichen aus dem FKT-Segment die 04 (unverändert) eingetragen hat und die Kasseninstitution bereits abgespeichert ist
	 * 
	 * @param importFileValidityFrom GültigAb-Datum der Importdatei
	 * @return IDKs die bearbeitet werden müssen 
	 */
	private List<IDK> filterIDKs(LocalDate importFileValidityFrom, CareProviderMethod careProviderMethod) {
		List<IDK> filterdIDKs = new ArrayList<>();
		Map<Integer, CostUnitInstitution> existingInstitutions = rFactory.getCostUnitInstitutionRepositoryCustom().findIKToLatestInstituinMapByCareProviderId(careProviderMethod.getId());
		
		for (IDK idk : listIDKs) {
			LocalDate validityUntil = idk.getVDT().getValidityUntil();
			
			//1.Liegt das GülitgBis-Datum vor dem GültigAb-Datum der Importdatei ?
			if (validityUntil != null && validityUntil.compareTo(importFileValidityFrom) <= 0) {
				continue;
			}
			
			//2.Ist das Verarbeitungskennzeichen aus dem FKT-Segment die 04 (unverändert) eingetragen und ist die Kasseninstitution bereits abgespeichert ?
			if(idk.getFKT().getProcessingIndicator().equals("04") && existingInstitutions.get(idk.getInstitutionCode()) != null) {
				continue;
			}
			filterdIDKs.add(idk);
		}
		return filterdIDKs;
	}
	
	/**
	 * Schließt (setzt das GültigBis-Datum auf das GültigAb-Datum der Importdatei) alle in der @param filterdIDKs befindelichen Kasseninstitutionen.
	 */
	private void closeInstitutions(List<IDK> filterdIDKs, LocalDate importFileValidityFrom, CareProviderMethod careProviderMethod) {
		Map<Integer, CostUnitInstitution> existingInstitutions = rFactory.getCostUnitInstitutionRepositoryCustom().findIKToLatestInstituinMapByCareProviderId(careProviderMethod.getId());
		
		for (IDK idk : filterdIDKs) {
			CostUnitInstitution institutionToClose = existingInstitutions.get(idk.getInstitutionCode());
			institutionToClose.setValidityUntil(importFileValidityFrom.minusDays(1));
			rFactory.getCostUnitInstitutionRepository().save(institutionToClose);
		}
	}
	
	private void updateInstitutions(List<IDK> filterdIDKs, LocalDate importFileValidityFrom, CareProviderMethod careProviderMethod) {
		Map<Integer, CostUnitInstitution> institutionMap = rFactory.getCostUnitInstitutionRepositoryCustom().findIKToLatestInstituinMapByCareProviderId(careProviderMethod.getId());
		
		for (IDK idk : filterdIDKs) {
			CostUnitInstitution existingInstitution = institutionMap.get(idk.getInstitutionCode());
			CostUnitInstitution institutionFromFile = idk.buildCostUnitInstitution(careProviderMethod);
			if (existingInstitution == null) {//Neue Institution
				rFactory.getCostUnitAddressRepository().save(institutionFromFile.getAddress()); //TODO 
				rFactory.getCostUnitInstitutionRepository().save(institutionFromFile); //TODO falls neue Addresse vorhanden ist soll diese auch direkt abgespeichert werden
			} else {
				institutionFromFile.setId(existingInstitution.getId());
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
}	
