package costunitimport.fileimport;

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
import java.util.stream.Stream;
import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.InternalServiceApplication;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitFile;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.DTACostUnitSeparation;
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
			throw new InternalServiceApplication("Lesen der Datei fehlerhaft!", e); 
		}
		insertCostUnitFile();
	}

	public void assign(String line) {
		final String[] data = splitLine(line);
		
		switch (data[0]) {
			case "IDK":
				IDK idk = new IDK(data);
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
		DTACostUnitSeparation costUnitSeperation = newFile.getDtaCostUnitSeparation();
		
		Map<Integer, CostUnitInstitution> existingInstitutionMap = rFactory.getCostUnitInstitutionRepositoryCustom().findIKToLatestInstituinMapByCareProviderIdAndCostUnitSeparationId(careProviderMethod.getId(), costUnitSeperation.getId());
		
		//*** Institutionen
		
		List<IDK> filterdIDKs = filterIDKs(newFile.getValidityFrom(), existingInstitutionMap);
		
		closeInstitutions(filterdIDKs, newFile.getValidityFrom(), existingInstitutionMap);
		
		updateInstitutions(filterdIDKs, careProviderMethod, costUnitSeperation, existingInstitutionMap);
		
		
		//*** Verknüpfungen
		
		/* Jetzt werden die aktualisierten Kasseninstitutionen geladen */
		Map<Integer, CostUnitInstitution> refreshedInstitutionMap = rFactory.getCostUnitInstitutionRepositoryCustom().findIKToLatestInstituinMapByCareProviderIdAndCostUnitSeparationId(careProviderMethod.getId(), costUnitSeperation.getId());
		
		/* Zu den sonsitgen Leistungserbringern 5 - alle Abrechnungscodes beschaffen (Abrechnungscode identifiziert eine Leistungserbringerart) */
		Map<Integer, DTAAccountingCode> idToAccountoungCode = rFactory.getAccountingCodeRepositoryCustom().findIDToDTAAccountingCodesByCareProviderMethodId(careProviderMethod.getId());
		
		for (IDK idk : filterdIDKs) {
			List<CostUnitAssignment> assignmentsFromFile = idk.getCostUnitAssignment(newFile.getValidityFrom(), refreshedInstitutionMap, idToAccountoungCode);
			
			CostUnitInstitution currentInstitution = refreshedInstitutionMap.get(idk.getInstitutionCode());
			
			List<CostUnitAssignment> exisitingAssignments = rFactory.getCostUnitAssignmentRepository().findByInstitutionIdAndValidityFrom(currentInstitution.getId(), newFile.getValidityFrom());
			if (!exisitingAssignments.isEmpty() && exisitingAssignments.stream().filter(assignment -> assignment.getValidityFrom().equals(newFile.getValidityFrom())).count() > 0) {
				//Die aktuell hinterlegten Verknüpfungen haben die gleiche GültigkeitAb wie die Datensätze die nun importiert werden sollen
				//Der Fall kann entstehen, 
			}
			closeAssignments(exisitingAssignments, newFile.getValidityFrom());
			updateAssignments(assignmentsFromFile, exisitingAssignments, newFile.getValidityFrom());
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
	private List<IDK> filterIDKs(LocalDate importFileValidityFrom, Map<Integer, CostUnitInstitution> existingInstitutionMap) {
		List<IDK> filterdIDKs = new ArrayList<>();
		
		for (IDK idk : listIDKs) {
			LocalDate validityUntil = idk.getVDT().getValidityUntil();
			
			//1.Liegt das GülitgBis-Datum vor dem GültigAb-Datum der Importdatei ?
			if (validityUntil != null && validityUntil.compareTo(importFileValidityFrom) <= 0) {
				continue;
			}
			
			//2.Ist das Verarbeitungskennzeichen aus dem FKT-Segment die 04 (unverändert) eingetragen und ist die Kasseninstitution bereits abgespeichert ?
			if(idk.getFKT().getProcessingIndicator().equals("04") && existingInstitutionMap.get(idk.getInstitutionCode()) != null) {
				continue;
			}
			filterdIDKs.add(idk);
		}
		return filterdIDKs;
	}
	
	private void updateInstitutions(List<IDK> filterdIDKs, CareProviderMethod careProviderMethod, DTACostUnitSeparation costUnitSeperation, Map<Integer, CostUnitInstitution> existingInstitutionMap) {
		for (IDK idk : filterdIDKs) {
			CostUnitInstitution existingInstitution = existingInstitutionMap.get(idk.getInstitutionCode());
			CostUnitInstitution institutionFromFile = idk.buildCostUnitInstitution(careProviderMethod, costUnitSeperation);
			
			if(existingInstitution != null) {
				institutionFromFile.setId(existingInstitution.getId());
			}
			rFactory.getCostUnitInstitutionRepository().save(institutionFromFile);
		}
	}
	
	/**
	 * Schließt (setzt das GültigBis-Datum auf das GültigAb-Datum der Importdatei) alle in der @param filterdIDKs befindelichen Kasseninstitutionen.
	 * 
	 * @param filterdIDKs gefilterte Kasseninstitutionen der Importdatei
	 * @param importFileValidityFrom GültigAb-Datum der Importdatei 
	 * @param existingInstitutionMap alle Institutionen aus der Datenbank
	 */
	
	//TODO Nur Institutionen abschließen die nicht in der neuen Datei vorhanden sind, aber in der DB
	private void closeInstitutions(List<IDK> filterdIDKs, LocalDate importFileValidityFrom, Map<Integer, CostUnitInstitution> existingInstitutionMap) {
		for (IDK idk : filterdIDKs) {
			CostUnitInstitution institutionToClose = existingInstitutionMap.get(idk.getInstitutionCode());
			
			/* Institution befindet sich nicht in der Datenbank */
			if(institutionToClose != null) {
				institutionToClose.setValidityUntil(importFileValidityFrom.minusDays(1));
				rFactory.getCostUnitInstitutionRepository().save(institutionToClose);
			}
		}
	}
	
	/**
	 * Prüft, ob die Verknüpfung schon in der Datenbank vorhanden ist.<br>
	 * Falls dies der Fall ist, ID der Verknüpfung aus der Datenbank auf die Verknüpfung in der Datei übertragen.<br>
	 * Somit muss kein neuer Datensatz erstellt werden.
	 * 
	 * @param assignmentsFromFile alle Verknüpfungen der Datei
	 * @param exisitingAssignments alle Verknüpfungen aus der Datenbank
	 * @param importFileValidityFrom GültigAb-Datum der Importdatei 
	 */
	private void updateAssignments(List<CostUnitAssignment> assignmentsFromFile, List<CostUnitAssignment> exisitingAssignments, LocalDate importFileValidityFrom) {
		for(CostUnitAssignment assignmentFromFile : assignmentsFromFile) {
			
			String assignmentFromFileCompareString = getCompareString(assignmentFromFile);
			
			for(CostUnitAssignment exisitingAssignment : exisitingAssignments) {
				if(assignmentFromFileCompareString.equals(getCompareString(exisitingAssignment))) {
					assignmentFromFile.setId(exisitingAssignment.getId());
				}
			}
		}
		rFactory.getCostUnitAssignmentRepository().saveAll(assignmentsFromFile);
	}
	
	/**
	 * Schließt alle Verknüpfungen der Datenbank.<br>
	 * Diese werden später durch die Verknüpfungen der Datei aktualisiert.
	 * 
	 * @param exisitingAssignments alle Verknüpfungen aus der Datenbank
	 * @param importFileValidityFrom GültigAb-Datum der Importdatei 
	 */
	private void closeAssignments(List<CostUnitAssignment> exisitingAssignments, LocalDate importFileValidityFrom) {
		LocalDate validityUntil = importFileValidityFrom.minusDays(1);
		for (CostUnitAssignment assignmentToClose : exisitingAssignments) {
			if (assignmentToClose.getValidityFrom().compareTo(validityUntil) >= 0) {
				//Eventuell für Verknüpfungen mit der Veknüpfungsart 01-Verweis vom IK der Versichertenkarte zum Kostenträger kann
				//dies manchmal vorkommen, dass eine Institution sich in mehreren gleichzeit gültigen Kostenträgerdateien befindet
				if (assignmentToClose.getTypeAssignment().getId() != 1) {
					throw new IllegalArgumentException("Fehlerhafte Verknüpfungen!!! KotrInstitutionId:" + assignmentToClose.getInstitutionId() + " kotrAssignmentId:" + assignmentToClose.getId());
				}
			} else {
				assignmentToClose.setValidityUntil(validityUntil);
			}
		}
		rFactory.getCostUnitAssignmentRepository().saveAll(exisitingAssignments);
	}
	
	//Man könnte auch die CompareKey-Methode in der CostUnitAssignment verwenden, jedoch müssen hierbei die Gültigkeiten ausgelassen und die Abrechnungscodes beachtet werden.
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
		builder.append("accountingCodes:").append(assignment.getAccountingCodes().toString());
		return builder.toString();
	}
}	