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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.InternalServiceApplication;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitFile;
import costunitimport.model.CostUnitInstitution;
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

public class FileImport {

	private final RepositoryFactory rFactory;

	private final Path path;
	private final List<IDK> listIDKs = new ArrayList<>();

	/**
	 * Das Gültigkeitsdatum welches bei einer Kostenträgerdatei mitgegeben wird.
	 * 
	 * Leider nicht schön....
	 */
	private final LocalDate validityFrom;

	private UNB unb;

	public FileImport(final Path path, final RepositoryFactory repositoryFactory, final LocalDate validityFrom) {
		this.path = Objects.requireNonNull(path);
		this.rFactory = Objects.requireNonNull(repositoryFactory);
		this.validityFrom = validityFrom;
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
			nam.ifPresent(x -> x.addANS(new ANS(data)));
			break;
		case "ASP":
			getLastIDK().addASP(new ASP(data));
			break;
		case "UEM":
			getLastIDK().addUEM(new UEM(data, rFactory));
			break;
		case "DFU":
			List<UEM> listUEMs = getLastIDK().getUEMs();
			listUEMs.get(listUEMs.size() - 1).getDFUs().add(new DFU(data));
			break;
		case "KTO":
			getLastIDK().addKTO(new KTO(data));
			break;
		case "UNB":
			this.unb = new UNB(data, rFactory);
			break;
		default:
			// keine Verarbeitung
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
		// Baut die Kostenträgerdatei
		CostUnitFile file = unb.buildCostUnitFile();
		
		// Leistungserbringergruppe (z.B 5 -> für sonstige Leistungserbringer)
		CareProviderMethod cpm = file.getCareProviderMethod();
		
		// Kassentrennung (z.B AOK)
		DTACostUnitSeparation costUnitSeperation = file.getDtaCostUnitSeparation();
		
		List<IDK> filterdIDKs = filterIDKs(validityFrom, cpm, costUnitSeperation);
		
		// *** Institutionen
		updateInstitutions(filterdIDKs, cpm, costUnitSeperation);

		// *** Verknüpfungen
		for (IDK idk : filterdIDKs) {
			List<CostUnitAssignment> assignmentsFromFile = idk.buildCostUnitAssignment(validityFrom, cpm,
					costUnitSeperation);
			updateAssignments(assignmentsFromFile, idk.getInstitutionCode());
		}
		rFactory.getFileRepository().save(file);
	}

	/**
	 * Filtert die IDKs der Importdatei.<br>
	 * <br>
	 * 
	 * Filterung :<br>
	 * IDK wird gefiltert wenn,<br>
	 * <br>
	 * 
	 * 1. Das GülitgBis-Datum vor dem GültigAb-Datum der Importdatei liegt<br>
	 * 2. Das Verarbeitungskennzeichen aus dem FKT-Segment die 04 (unverändert)
	 * eingetragen hat und die Kasseninstitution bereits abgespeichert ist
	 * 
	 * @param importFileValidityFrom GültigAb-Datum der Importdatei
	 * @return IDKs die bearbeitet werden müssen
	 */
	private List<IDK> filterIDKs(LocalDate importFileValidityFrom, CareProviderMethod cpm, DTACostUnitSeparation costUnitSeperation) {
		List<IDK> filterdIDKs = new ArrayList<>();
		
		/*
		 * Sucht alle Kasseninstitutionen nach Leistungserbringerschlüssel (Bsp: 5 für
		 * Sonstige Leistungserbringer) und Kassentrennung (Bsp: AOK))
		 */
		Map<Integer, CostUnitInstitution> existingInstitutionMap = rFactory.getCostUnitInstitutionRepositoryCustom()
				.findIKToLatestInstituinMapByCareProviderIdAndCostUnitSeparationId(cpm.getId(),
						costUnitSeperation.getId());

		for (IDK idk : listIDKs) {
			LocalDate validityUntil = idk.getVDT().getValidityUntil();

			// 1.Liegt das GülitgBis-Datum vor dem GültigAb-Datum der Importdatei ?
			if (validityUntil != null && validityUntil.compareTo(importFileValidityFrom) <= 0) {
				continue;
			}

			// 2.Ist das Verarbeitungskennzeichen aus dem FKT-Segment die 04 (unverändert)
			// eingetragen und ist die Kasseninstitution bereits abgespeichert ?
			if (idk.getFKT().getProcessingIndicator().equals("04")
					&& existingInstitutionMap.get(idk.getInstitutionCode()) != null) {
				continue;
			}
			filterdIDKs.add(idk);
		}
		return filterdIDKs;
	}
	
	/**
	 * Schließt (setzt das GültigBis-Datum auf das GültigAb-Datum der Importdatei)<br>
	 * alle Kasseninstitutionen die sich in der Datenbank befinden aber nicht mehr<br>
	 * in der Importdatei.<br>
	 * Prüft danach, ob die Institution aus der Datei schon in der Datenbank vorhanden ist.<br>
	 * Falls dies der Fall ist, ID der Instituition aus der Datenbank auf die
	 * Instituition in der Datei übertragen.<br>
	 * Somit muss kein neuer Datensatz erstellt werden.
	 * 
	 * @param listIDKs Kasseninstitutionen der Importdatei
	 */
	private void updateInstitutions(List<IDK> filterdIDKs, CareProviderMethod cpm, DTACostUnitSeparation costUnitSeperation) {
		/*
		 * Sucht alle Kasseninstitutionen nach Leistungserbringerschlüssel (Bsp: 5 für
		 * Sonstige Leistungserbringer) und Kassentrennung (Bsp: AOK))
		 */
		Map<Integer, CostUnitInstitution> existingInstitutionMap = rFactory.getCostUnitInstitutionRepositoryCustom()
				.findIKToLatestInstituinMapByCareProviderIdAndCostUnitSeparationId(cpm.getId(),
						costUnitSeperation.getId());
		
		for (CostUnitInstitution existingInstitution : existingInstitutionMap.values()) {
			List<Integer> idkIKs = listIDKs.stream().map(IDK::getInstitutionCode).collect(Collectors.toList());

			if (!idkIKs.contains(existingInstitution.getInstitutionNumber())) {
				existingInstitution.setValidityUntil(validityFrom);
				rFactory.getCostUnitInstitutionRepository().save(existingInstitution);
			}
		}
		
		for (IDK idk : filterdIDKs) {
			CostUnitInstitution existingInstitution = existingInstitutionMap.get(idk.getInstitutionCode());
			CostUnitInstitution institutionFromFile = idk.buildCostUnitInstitution(cpm,
					costUnitSeperation);

			if (existingInstitution != null) {
				institutionFromFile.setId(existingInstitution.getId());
			}
			rFactory.getCostUnitInstitutionRepository().save(institutionFromFile);
		}
	}

	/**
	 * Schließt erst alle Verknüpfungen auf der Datenbank.<br>
	 * Prüft danach, ob die Verknüpfung aus der Datei schon in der Datenbank vorhanden ist.<br>
	 * Falls dies der Fall ist, ID der Verknüpfung aus der Datenbank auf die
	 * Verknüpfung in der Datei übertragen.<br>
	 * Somit muss kein neuer Datensatz erstellt werden.
	 * 
	 * @param assignmentsFromFile    alle Verknüpfungen der Datei
	 * @param currentInstitution 
	 * @param importFileValidityFrom GültigAb-Datum der Importdatei
	 */
	private void updateAssignments(List<CostUnitAssignment> assignmentsFromFile, Integer institutionCode) {
		CostUnitInstitution currentInstitution = rFactory.getCostUnitInstitutionRepositoryCustom()
				.findLatestCostUnitInstitutionByInstitutionNumber(institutionCode).orElseThrow();
		
		List<CostUnitAssignment> exisitingAssignments = rFactory.getCostUnitAssignmentRepository()
				.findByParentInstitutionIdAndValidityFrom(currentInstitution.getId(), validityFrom);
		
		LocalDate validityUntil = validityFrom.minusDays(1);
		exisitingAssignments.stream().forEach(x -> x.setValidityUntil(validityUntil));
		rFactory.getCostUnitAssignmentRepository().saveAll(exisitingAssignments);
		
		for (CostUnitAssignment assignmentFromFile : assignmentsFromFile) {

			String assignmentFromFileCompareString = getCompareString(assignmentFromFile);

			for (CostUnitAssignment exisitingAssignment : exisitingAssignments) {
				if (assignmentFromFileCompareString.equals(getCompareString(exisitingAssignment))) {
					assignmentFromFile.setId(exisitingAssignment.getId());
				}
			}
		}
		rFactory.getCostUnitAssignmentRepository().saveAll(assignmentsFromFile);
	}

	// Man könnte auch die CompareKey-Methode in der CostUnitAssignment verwenden,
	// jedoch müssen hierbei die Gültigkeiten ausgelassen und die Abrechnungscodes
	// beachtet werden.
	private static String getCompareString(CostUnitAssignment assignment) {
		StringBuilder builder = new StringBuilder();
		builder.append("typeAssignment:")
				.append(assignment.getTypeAssignmentId() == null ? null : assignment.getTypeAssignmentId()).append("|");
		builder.append("institutionId:").append(assignment.getParentInstitutionId()).append("|");
		builder.append("institutionIdAssignment:").append(assignment.getInstitutionIdAssignment()).append("|");
		builder.append("institutionIdAccounting:").append(assignment.getInstitutionIdAccounting()).append("|");
		builder.append("typeDataSupply:")
				.append(assignment.getTypeDataSupply() == null ? null : assignment.getTypeDataSupply().getId())
				.append("|");
		builder.append("typeMedium:").append(assignment.getTypeMediumId() == null ? null : assignment.getTypeMediumId())
				.append("|");
		builder.append("federalStateClassificationId:").append(assignment.getFederalStateClassificationId())
				.append("|");
		builder.append("districtId:").append(assignment.getDistrictId()).append("|");
		builder.append("rateCode:").append(assignment.getRateCode()).append("|");
		builder.append("accountingCodes:").append(assignment.getAccountingCodes().toString());
		return builder.toString();
	}
}
