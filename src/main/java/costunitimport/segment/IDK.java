package costunitimport.segment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.address.Address;

public class IDK extends Segment{
	
	private Integer institutionCode;
	private Integer kindOfInstitution;
	private String description;
	private Integer vKNR;

	private VDT vdt;
	private FKT fkt;
	private Optional<NAM> nam;
	
	private List<VKG> listVKG = new ArrayList<>();
	private List<KTO> costUnitFileKTOs = new ArrayList<>();
	private List<ASP> costUnitFileASPs = new ArrayList<>();
	private List<UEM> costUnitFileUEMs = new ArrayList<>();
	
	private RepositoryFactory rFactory;
	
	public IDK(String[] data, RepositoryFactory rFactory) {
		super(data);
		this.rFactory = rFactory;
	}

	@Override
	protected void assignData() {
		int position = 1;
		institutionCode = getData(position++, Integer.class);
		kindOfInstitution = getData(position++, Integer.class);
		description = getData(position++, String.class);
		vKNR = getData(position, Integer.class);
	}
	
	/**
	 * Institutionskennzeichen
	 * 
	 * @return Institutionskennzeichen
	 */
	public Integer getInstitutionCode() {
		return institutionCode;
	}

	/**
	 * Art der Institution<br>
	 * Schlüssel Art der Institution = 99 (Dummy)
	 * 
	 * @return Art der Institution
	 */
	public Integer getKindOfInstitution() {
		return kindOfInstitution;
	}

	/**
	 * Kurzbezeichnung
	 * 
	 * @return Kurzbezeichnung
	 */
	public String getShortDescription() {
		return description;
	}

	/**
	 * VKNR<br>
	 * Angabe ist nicht erforderlich, auch, wenn es sich bei der Institution um eine Krankenkasse handelt
	 * 
	 * @return VKNR
	 */
	public Integer getvKNR() {
		return vKNR;
	}


	public List<VKG> getCostUnitFileVKGs() {
		return listVKG;
	}


	public void setCostUnitFileVKGs(List<VKG> costUnitFileVKGs) {
		this.listVKG = costUnitFileVKGs;
	}


	public FKT getFKT() {
		return Objects.requireNonNull(fkt);
	}


	public void setFKT(FKT fkt) {
		this.fkt =fkt;
	}


	public List<KTO> getCostUnitFileKTOs() {
		return costUnitFileKTOs;
	}


	public void setCostUnitFileKTOs(List<KTO> costUnitFileKTOs) {
		this.costUnitFileKTOs = costUnitFileKTOs;
	}


	public Optional<NAM> getNAM() {
		return nam;
	}


	public void setNAM(NAM nam) {
		this.nam = Optional.ofNullable(nam);
	}


	public List<ASP> getCostUnitFileASPs() {
		return costUnitFileASPs;
	}


	public void setCostUnitFileASPs(List<ASP> costUnitFileASPs) {
		this.costUnitFileASPs = costUnitFileASPs;
	}


	public List<UEM> getCostUnitFileUEMs() {
		return costUnitFileUEMs;
	}


	public void setCostUnitFileUEMs(List<UEM> costUnitFileUEMs) {
		this.costUnitFileUEMs = costUnitFileUEMs;
	}


	public VDT getVDT() {
		return Objects.requireNonNull(vdt);
	}


	public void setCostUnitFileVDT(VDT vdt) {
		this.vdt = vdt;
	}
	
	public List<CostUnitAssignment> getCostUnitAssignment(LocalDate validityFrom, Map<Integer, CostUnitInstitution> costUnitInstitutions, Map<Integer, DTAAccountingCode> mapAccountingCodesCareProviderMethod){
		List<CostUnitAssignment> allAssignments = new ArrayList<>();
		
		/* Mappt die VKGs nach Schlüssel Art der Verknüpfung. Bswp -> 02 - Verweis auf eine Datenannahmestelle*/
		Map<Integer, List<VKG>> mapByKindOfAssignment = listVKG.stream().collect(Collectors.groupingBy(VKG::getKindOfAssignment));
        
		/* Iteriert durch jede Schlüsselart der Verknüpfung*/
		for (Entry<Integer, List<VKG>> entry : mapByKindOfAssignment.entrySet()) {
        	List<CostUnitAssignment> assignmentsByKindOfAssignment = new ArrayList<>();
        	List<VKG> vkgs = entry.getValue().stream().sorted(Comparator.comparing(VKG::getAccountingCode)).collect(Collectors.toList());
        	
    		/* Das sind die Ids der Obergruppen der Abrechnungscodes bspw : 
    		   00 Sammelschlüssel für alle Leistungsarten , 10 Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)*/
    		int[] groupAccountCodes = DTAAccountingCode.getGroupAccountingCodes(); 
        	
        	//*** Alle Datensätze mit korrekten Abrechnungscodes -> das heißt alle Verknüpfungen ohne Gruppenschlüssel
			List<VKG> listVKGsWithoutGroupACs = vkgs.stream().filter(v -> v.getAccountingCode() != null)
					.filter(v -> IntStream.of(groupAccountCodes).noneMatch(any -> any == v.getAccountingCode().intValue())).collect(Collectors.toList());
			for (VKG vkg : listVKGsWithoutGroupACs) {
				if (vkg.getAccountingCode() == 18 || vkg.getAccountingCode() == 60) {
					/*
					 * 18-Sanitätshaus (Bei neuen Verträgen bzw. Vertragsanpassungen ist eine Umschlüsselung mit dem Abrechnungscode 15
					 * vorzunehmen. Der Abrechnungscode 18 wird für Sanitätshäuser zum 31.12.2005 aufgehoben.)
					 * Betriebshilfe 60 ist keinem SAGS zugeordnet!
					 */
					continue;
				}
				CostUnitAssignment assignment = vkg.buildCostUnitAssignment(validityFrom, costUnitInstitutions);
				assignment.setAccountingCodes(getDtaAccountCodes(List.of(vkg.getAccountingCode().toString())));
				assignmentsByKindOfAssignment.add(assignment);
			} //***
        	         	 
			for (VKG vkg : vkgs) {
				if (!listVKGsWithoutGroupACs.contains(vkg)) {
					
					CostUnitAssignment groupAssignment = vkg.buildCostUnitAssignment(validityFrom, costUnitInstitutions);
					
					if (vkg.getAccountingCode() == null) {
						groupAssignment.setAccountingCodes(new ArrayList<DTAAccountingCode>());//00-Sammelschlüssel für alle Leistungsarten
						assignmentsByKindOfAssignment.add(groupAssignment);
					} else {
						switch (vkg.getAccountingCode()) {
							case 0://00-Sammelschlüssel für alle Leistungsarten
								groupAssignment.setAccountingCodes(new ArrayList<DTAAccountingCode>());//00-Sammelschlüssel für alle Leistungsarten
								break;
							case 10://10-Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)
								groupAssignment.setAccountingCodes(getDtaAccountCodes(List.of("11", "12", "13", "14", "15", "16", "17", "19")));
								break;
							case 20://20-Gruppenschlüssel Heilmittelerbringer (Schlüssel 21-29)
								groupAssignment.setAccountingCodes(getDtaAccountCodes(List.of("21", "22", "23", "24", "25", "26", "27", "28", "29")));
								break;
							case 30://30-Gruppenschlüssel Häusliche Krankenpflege (Schlüssel 31-34)
								groupAssignment.setAccountingCodes(getDtaAccountCodes(List.of("31", "32", "33", "34")));
								break;
							case 40://40-Gruppenschlüssel Krankentransportleistungen (Schlüssel 41-49)
								groupAssignment.setAccountingCodes(getDtaAccountCodes(List.of("41", "42", "43", "44", "45", "46", "47", "48", "49")));
								break;
							case 99://99-Sonderschlüssel, gilt für alle in der Kostenträgerdatei nicht aufgeführten Gruppen- und Einzelschlüssel
								List<DTAAccountingCode> listAllocatedACs = assignmentsByKindOfAssignment.stream().filter(v -> v.getAccountingCodes() != null)
										.map(CostUnitAssignment::getAccountingCodes).flatMap(List::stream).collect(Collectors.toList());
								List<DTAAccountingCode> listRemainingdACs = mapAccountingCodesCareProviderMethod.values().stream().filter(ac -> !listAllocatedACs.contains(ac))
										.collect(Collectors.toList());
								groupAssignment.setAccountingCodes(listRemainingdACs);
								break;
							default:
								break;
						}
						assignmentsByKindOfAssignment.add(groupAssignment);
					}
				}
			}
			allAssignments.addAll(assignmentsByKindOfAssignment);
		}

		Map<String, CostUnitAssignment> mapGroupedCostUnitAssignments = new HashMap<>();
		for (CostUnitAssignment currentAssignment : allAssignments) {
			StringBuilder keyBuilder = new StringBuilder();
			keyBuilder.append("Id:").append(currentAssignment.getId());
			keyBuilder.append("TypeAssignment:").append(currentAssignment.getTypeAssignment());
			keyBuilder.append("InstitutionId:").append(currentAssignment.getInstitutionId());
			keyBuilder.append("InstitutionIdAssignment:").append(currentAssignment.getInstitutionIdAssignment());
			keyBuilder.append("InstitutionIdAccounting:").append(currentAssignment.getInstitutionIdAccounting());
			keyBuilder.append("TypeDataSupply:").append(currentAssignment.getTypeDataSupply());
			keyBuilder.append("TypeMedium:").append(currentAssignment.getTypeMedium());
			keyBuilder.append("FederalStateClassificationId:").append(currentAssignment.getFederalStateClassificationId());
			keyBuilder.append("DistrictId:").append(currentAssignment.getDistrictId());
			keyBuilder.append("RateCode:").append(currentAssignment.getRateCode());
			keyBuilder.append("ValidityFrom:").append(currentAssignment.getValidityFrom());
			keyBuilder.append("ValidityUntil:").append(currentAssignment.getValidityUntil());

			CostUnitAssignment existingAssignment = mapGroupedCostUnitAssignments.get(keyBuilder.toString());
			if (existingAssignment == null) {
				mapGroupedCostUnitAssignments.put(keyBuilder.toString(), currentAssignment);
			} else if (existingAssignment.getAccountingCodes() != null) {
				if (currentAssignment.getAccountingCodes() == null) {
					//					throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Gruppierung der Verknüpfungen fehlgeschlagen!");
				}
				boolean exist = existingAssignment.getAccountingCodes().stream().anyMatch(a -> currentAssignment.getAccountingCodes().contains(a));
				if (exist) {
					//					throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Gruppierung der Verknüpfungen fehlgeschlagen!");
				}

				existingAssignment.getAccountingCodes().addAll(currentAssignment.getAccountingCodes());
			}
		}

		CostUnitInstitution currentCostUnitInstitution = costUnitInstitutions.get(getInstitutionCode());
		for (CostUnitAssignment currentAssignment : mapGroupedCostUnitAssignments.values()) {
			currentAssignment.setInstitutionId(currentCostUnitInstitution.getId());
		}
		return mapGroupedCostUnitAssignments.values().stream().collect(Collectors.toList());
	}
	
	private List<DTAAccountingCode> getDtaAccountCodes(List<String> searchACs) {
		Objects.requireNonNull(searchACs);

		List<DTAAccountingCode> listAccountingCodes = new ArrayList<>();
		for (String currentAC : searchACs) {
			Optional<DTAAccountingCode> accountingCode = rFactory.getAccountingCodeRepository().findById(Integer.valueOf(currentAC));
			listAccountingCodes.add(accountingCode.orElseThrow());
		}
		return listAccountingCodes;
	}
	
	/**
	 * Ermittelt anhand den eingelesenen Daten die mögliche Anschrift
	 * 
	 * @return Anschrift
	 * @throws IOException 
	 */
	public Optional<Address> buildCostUnitAddress() {
		Address addressZip = null;
		Address addressPostCode = null;
		if (nam.isPresent() && nam.get().getCostUnitFileANSs() != null && !nam.get().getCostUnitFileANSs().isEmpty()) {//NAM-Segment: einmal obligatorisch und Adressen vorhanden
			LocalDate validityFrom = getVDT().getValidityFrom();
			LocalDate validityUntil = getVDT().getValidityUntil();
			for (ANS ans : nam.get().getCostUnitFileANSs()) {
				Address addressTmp = ans.getODAContactAddress(validityFrom, validityUntil);
				if (ans.getKindOfAddress().intValue() == 1) { //Hausanschrift
					addressZip  = addressTmp;
				} else if (ans.getKindOfAddress().intValue() == 2) { //Postfachanschrift
					addressPostCode = addressTmp;
				}
			}
			if (addressZip != null && addressPostCode != null) {
				addressZip.setPostBox(addressPostCode.getPostBox());
				return Optional.of(addressZip);
			}
		}
		return addressZip != null ? Optional.of(addressZip) : Optional.ofNullable(addressPostCode);
	}
	
	public CostUnitInstitution buildCostUnitInstitution(CareProviderMethod careProviderMethod) {
		CostUnitInstitution institution = new CostUnitInstitution();
//		institution.setActiveIndicator(Boolean.TRUE);//in der Datei befinden sich nur aktuell gültige Institutionen -> unrelevant
		institution.setCareProviderMethodId(careProviderMethod.getId());//wird gesetzt aus den Informationen aus dem UNB-Segment
		institution.setCreationTime(LocalDateTime.now());
		institution.setValidityFrom(getVDT().getValidityFrom());
		institution.setValidityUntil(getVDT().getValidityUntil());
		institution.setFirmName(buildFirmName());
		institution.setInstitutionNumber(institutionCode);
		institution.setVknr(vKNR);
		institution.setAddress(buildCostUnitAddress().orElse(null));
		institution.setShortDescription(description);
//		ODAContactType contactType = FacadeHandler.getMasterDataInfFacadeLocal().findODAContactTypeById(ODAContactType.KOTR_INSTITUTION);
//		institution.setODAContactType(contactType);
//		institution.setMembership(Integer.valueOf(0)); -> unrelevant
		return institution;
	}
	
	private String buildFirmName() {
		if (nam.isPresent()) {//NAM-Segment: einmal obligatorisch
			String[] names = new String[] {nam.get().getName1(), nam.get().getName2(), nam.get().getName3(), nam.get().getName4()};
			String collectedName = Arrays.stream(names).filter(s -> s!=null && !s.trim().isEmpty()).collect(Collectors.joining(" "));
			if(collectedName!=null && !collectedName.isEmpty()) {
				return collectedName;
			}
		}
		return description;
	}

}
