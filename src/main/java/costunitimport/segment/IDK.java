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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import costunitimport.model.AccountingCode;
import costunitimport.model.Address;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;

public class IDK extends Segment{
	
	private Integer institutionCode;
	private Integer kindOfInstitution;
	private String shortDescription;
	private Integer vKNR;

	private Optional<VDT> costUnitFileVDT = Optional.empty();
	
	private List<VKG> costUnitFileVKGs = new ArrayList<>();
	private FKT costUnitFileFKT = null;
	private List<KTO> costUnitFileKTOs = new ArrayList<>();
	private NAM costUnitFileNAM = null;
	private List<ASP> costUnitFileASPs = new ArrayList<>();
	private List<UEM> costUnitFileUEMs = new ArrayList<>();
	
	public IDK(String[] data) {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		institutionCode = getData(position++, Integer.class);
		kindOfInstitution = getData(position++, Integer.class);
		shortDescription = getData(position++, String.class);
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
		return shortDescription;
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
		return costUnitFileVKGs;
	}


	public void setCostUnitFileVKGs(List<VKG> costUnitFileVKGs) {
		this.costUnitFileVKGs = costUnitFileVKGs;
	}


	public FKT getCostUnitFileFKT() {
		return costUnitFileFKT;
	}


	public void setCostUnitFileFKT(FKT costUnitFileFKT) {
		this.costUnitFileFKT = costUnitFileFKT;
	}


	public List<KTO> getCostUnitFileKTOs() {
		return costUnitFileKTOs;
	}


	public void setCostUnitFileKTOs(List<KTO> costUnitFileKTOs) {
		this.costUnitFileKTOs = costUnitFileKTOs;
	}


	public NAM getCostUnitFileNAM() {
		return costUnitFileNAM;
	}


	public void setCostUnitFileNAM(NAM costUnitFileNAM) {
		this.costUnitFileNAM = costUnitFileNAM;
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


	public Optional<VDT> getCostUnitFileVDT() {
		return costUnitFileVDT;
	}


	public void setCostUnitFileVDT(VDT costUnitFileVDT) {
		this.costUnitFileVDT = Optional.ofNullable(costUnitFileVDT);
	}
	
	public List<CostUnitAssignment> getCostUnitAssignment(LocalDate validityFrom, Map<Integer, CostUnitInstitution> kotrInstitutions, Map<String, AccountingCode> mapAccountingCodesCareProviderMethod){
		//Das sind die Ids der Obergruppen der Abrechnungscodes bspw : 
		//00 Sammelschlüssel für alle Leistungsarten , 10 Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)
		int[] specialACs = new int[] {0,99,10,20,30,40}; 
		
		List<CostUnitAssignment> allAssignments = new ArrayList<>();
		Map<Integer, List<VKG>> mapByKindOfAssignment = costUnitFileVKGs.stream().collect(Collectors.groupingBy(VKG::getKindOfAssignment));
        for (Entry<Integer, List<VKG>> entry : mapByKindOfAssignment.entrySet()) {
        	List<CostUnitAssignment> assignmentsByKindOfAssignment = new ArrayList<>();
        	List<VKG> fileVKGs = entry.getValue().stream().sorted(Comparator.comparing(VKG::getAccountingCode)).collect(Collectors.toList());
        	
        	//*** Alle Datensätze mit korrekten Abrechnungscodes
        	List<VKG> listVKGsWithCorrectACs =fileVKGs.stream().filter(v -> v.getAccountingCode()!=null).filter(v -> IntStream.of(specialACs).noneMatch(any -> any == v.getAccountingCode().intValue())).collect(Collectors.toList());
        	 for (VKG vkg : listVKGsWithCorrectACs) {
        		 if(vkg.getAccountingCode()==18 || vkg.getAccountingCode()==60) {
        			 /* 18-Sanitätshaus (Bei neuen Verträgen bzw. Vertragsanpassungen ist eine Umschlüsselung mit dem Abrechnungscode 15 
        			  * vorzunehmen. Der Abrechnungscode 18 wird für Sanitätshäuser zum 31.12.2005 aufgehoben.)
        			  * Betriebshilfe 60 ist keinem SAGS zugeordnet!
        			  */
        			 continue;
        		 }
        		 CostUnitAssignment assignment = vkg.getCostUnitAssignment(validityFrom, kotrInstitutions);
        		 assignment.setAccountingCodes(getDtaAccountCodes(mapAccountingCodesCareProviderMethod, new String[] {vkg.getAccountingCode().toString()}));
        		 assignmentsByKindOfAssignment.add(assignment);
			}//***
        	         	 
        	 for (VKG costUnitFileVKG : fileVKGs) {
        		 CostUnitAssignment kotrAssignment = costUnitFileVKG.getCostUnitAssignment(validityFrom, kotrInstitutions);
				if(!listVKGsWithCorrectACs.contains(costUnitFileVKG)) {
					if(costUnitFileVKG.getAccountingCode()== null) {
						kotrAssignment.setAccountingCodes(new ArrayList<AccountingCode>());//00-Sammelschlüssel für alle Leistungsarten
						assignmentsByKindOfAssignment.add(kotrAssignment);
					} else {
						switch ( costUnitFileVKG.getAccountingCode()) {
							case 0://00-Sammelschlüssel für alle Leistungsarten
								kotrAssignment.setAccountingCodes(new ArrayList<AccountingCode>());//00-Sammelschlüssel für alle Leistungsarten
								break;
							case 10://10-Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)
								kotrAssignment.setAccountingCodes(getDtaAccountCodes(mapAccountingCodesCareProviderMethod, new String[] {"11","12","13","14","15","16","17","19"}));
								break;
							case 20://20-Gruppenschlüssel Heilmittelerbringer (Schlüssel 21-29)
								kotrAssignment.setAccountingCodes(getDtaAccountCodes(mapAccountingCodesCareProviderMethod, new String[] {"21","22","23","24","25","26","27","28","29"}));
								break;
							case 30://30-Gruppenschlüssel Häusliche Krankenpflege (Schlüssel 31-34)
								kotrAssignment.setAccountingCodes(getDtaAccountCodes(mapAccountingCodesCareProviderMethod, new String[] {"31","32","33","34"}));
								break;
							case 40://40-Gruppenschlüssel Krankentransportleistungen (Schlüssel 41-49)
								kotrAssignment.setAccountingCodes(getDtaAccountCodes(mapAccountingCodesCareProviderMethod, new String[] {"41","42","43","44","45","46","47","48","49"}));
								break;
							case 99://99-Sonderschlüssel, gilt für alle in der Kostenträgerdatei nicht aufgeführten Gruppen- und Einzelschlüssel
								List<AccountingCode> listAllocatedACs = assignmentsByKindOfAssignment.stream().filter(v -> v.getAccountingCodes()!=null).map(CostUnitAssignment::getAccountingCodes).flatMap(List::stream).collect(Collectors.toList());
								List<AccountingCode> listRemainingdACs = mapAccountingCodesCareProviderMethod.values().stream().filter(ac -> !listAllocatedACs.contains(ac)).collect(Collectors.toList());
								kotrAssignment.setAccountingCodes(listRemainingdACs);
								break;
							default:
								break;
						}
						assignmentsByKindOfAssignment.add(kotrAssignment);
					}
				}
			}
        	 allAssignments.addAll(assignmentsByKindOfAssignment);
		}
        
        Map<String, CostUnitAssignment> mapGroupedKotrAssignments = new HashMap<>();
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
			
			CostUnitAssignment existingAssignment = mapGroupedKotrAssignments.get(keyBuilder.toString());
			if(existingAssignment==null) {
				mapGroupedKotrAssignments.put(keyBuilder.toString(), currentAssignment);
			} else if(existingAssignment.getAccountingCodes()!=null){
				if(currentAssignment.getAccountingCodes()==null) {
//					throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Gruppierung der Verknüpfungen fehlgeschlagen!");
				}
				boolean exist = existingAssignment.getAccountingCodes().stream().anyMatch(a -> currentAssignment.getAccountingCodes().contains(a));
				if(exist) {
//					throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Gruppierung der Verknüpfungen fehlgeschlagen!");
				}
				
				existingAssignment.getAccountingCodes().addAll(currentAssignment.getAccountingCodes());
			}
		}
        
        
        CostUnitInstitution currentKotrInstitution = kotrInstitutions.get(getInstitutionCode());
		for (CostUnitAssignment currentAssignment : mapGroupedKotrAssignments.values()) {
        	currentAssignment.setInstitutionId(currentKotrInstitution.getId());
        }
        return mapGroupedKotrAssignments.values().stream().collect(Collectors.toList());
	}
	
	private List<AccountingCode> getDtaAccountCodes(Map<String, AccountingCode> mapAccountingCodesCareProviderMethod, String[] searchACs) {
		if (searchACs == null || searchACs.length <= 0) {
//			throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Keine ACs übergeben!");
		}
		List<AccountingCode> listAccountingCodes = new ArrayList<>();
		for (String currentAC : searchACs) {
			AccountingCode accountingCode = mapAccountingCodesCareProviderMethod.get(currentAC);
			if (accountingCode == null) {
//				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Kein korrekter Abrechnungscode! " + currentAC);
			}
			listAccountingCodes.add(accountingCode);
		}
		return listAccountingCodes;
	}

	/**
	 * Ermittelt anhand den eingelesenen Daten die mögliche Anschrift
	 * 
	 * @return Anschrift
	 * @throws IOException 
	 */
	public Address getCostUnitAddress() {
		Address addressZip = null;
		Address addressPostCode = null;
		if (costUnitFileNAM != null && costUnitFileNAM.getCostUnitFileANSs() != null && !costUnitFileNAM.getCostUnitFileANSs().isEmpty()) {//NAM-Segment: einmal obligatorisch und Adressen vorhanden
			LocalDate validityFrom = costUnitFileVDT.get().getValidityFrom() != null ? costUnitFileVDT.get().getValidityFrom() : null;
			LocalDate validityUntil = costUnitFileVDT.get().getValidityUntil() != null ? costUnitFileVDT.get().getValidityUntil() : null;
			for (ANS currentANS : costUnitFileNAM.getCostUnitFileANSs()) {
				Address addressTmp = currentANS.getODAContactAddress(validityFrom, validityUntil);
				if (currentANS.getKindOfAddress().intValue() == 1) {
					addressZip  = addressTmp;
				} else if (currentANS.getKindOfAddress().intValue() == 2) {
					addressPostCode = addressTmp;
				}
			}
			if (addressZip != null && addressPostCode != null) {
				addressZip.setPostBox(addressPostCode.getPostBox());
				return addressZip;
			}
		}
		return addressZip != null ? addressZip : addressPostCode;
	}
	
	public CostUnitInstitution getCostUnitInstitution(CareProviderMethod careProviderMethod) {
		CostUnitInstitution institution = new CostUnitInstitution();
//		institution.setActiveIndicator(Boolean.TRUE);//in der Datei befinden sich nur aktuell gültige Institutionen -> unrelevant
//		institution.setCareProviderMethod(careProviderMethod);//wird gesetzt aus den Informationen aus dem UNB-Segment -> unrelevant
		institution.setCreationTime(LocalDateTime.now());
		if (costUnitFileVDT.isPresent()) {
			institution.setValidityFrom(costUnitFileVDT.get().getValidityFrom());
			institution.setValidityUntil(costUnitFileVDT.get().getValidityUntil());
		}

		institution.setFirmName(shortDescription);
		if (costUnitFileNAM != null) {//NAM-Segment: einmal obligatorisch
			String[] names = {costUnitFileNAM.getName1(), costUnitFileNAM.getName2(), costUnitFileNAM.getName3(), costUnitFileNAM.getName4()};
			String collectedName = Arrays.stream(names).filter(s -> s!=null && !s.trim().isEmpty()).collect(Collectors.joining(" "));
			if(collectedName!=null && !collectedName.isEmpty()) {
				institution.setFirmName(collectedName);
			}
		}
		institution.setInstitutionNumber(institutionCode);
		institution.setVknr(vKNR);
		institution.setAddress(getCostUnitAddress());
		institution.setShortDescription(shortDescription);
//		ODAContactType contactType = FacadeHandler.getMasterDataInfFacadeLocal().findODAContactTypeById(ODAContactType.KOTR_INSTITUTION);
//		institution.setODAContactType(contactType);
//		institution.setMembership(Integer.valueOf(0)); -> unrelevant
		return institution;
	}

}