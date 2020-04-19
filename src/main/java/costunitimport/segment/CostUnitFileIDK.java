package costunitimport.segment;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
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

import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAddress;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;

public class CostUnitFileIDK extends CostUnitFileAbstract{
	
	private Integer institutionCode;
	private Integer kindOfInstitution;
	private String shortDescription;
	private Integer vKNR;

	private Optional<CostUnitFileVDT> costUnitFileVDT = Optional.empty();
	
	private List<CostUnitFileVKG> costUnitFileVKGs = new ArrayList<>();
	private CostUnitFileFKT costUnitFileFKT = null;
	private List<CostUnitFileKTO> costUnitFileKTOs = new ArrayList<>();
	private CostUnitFileNAM costUnitFileNAM = null;
	private List<CostUnitFileASP> costUnitFileASPs = new ArrayList<>();
	private List<CostUnitFileUEM> costUnitFileUEMs = new ArrayList<>();
	
	public CostUnitFileIDK(String[] data) {
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


	public List<CostUnitFileVKG> getCostUnitFileVKGs() {
		return costUnitFileVKGs;
	}


	public void setCostUnitFileVKGs(List<CostUnitFileVKG> costUnitFileVKGs) {
		this.costUnitFileVKGs = costUnitFileVKGs;
	}


	public CostUnitFileFKT getCostUnitFileFKT() {
		return costUnitFileFKT;
	}


	public void setCostUnitFileFKT(CostUnitFileFKT costUnitFileFKT) {
		this.costUnitFileFKT = costUnitFileFKT;
	}


	public List<CostUnitFileKTO> getCostUnitFileKTOs() {
		return costUnitFileKTOs;
	}


	public void setCostUnitFileKTOs(List<CostUnitFileKTO> costUnitFileKTOs) {
		this.costUnitFileKTOs = costUnitFileKTOs;
	}


	public CostUnitFileNAM getCostUnitFileNAM() {
		return costUnitFileNAM;
	}


	public void setCostUnitFileNAM(CostUnitFileNAM costUnitFileNAM) {
		this.costUnitFileNAM = costUnitFileNAM;
	}


	public List<CostUnitFileASP> getCostUnitFileASPs() {
		return costUnitFileASPs;
	}


	public void setCostUnitFileASPs(List<CostUnitFileASP> costUnitFileASPs) {
		this.costUnitFileASPs = costUnitFileASPs;
	}


	public List<CostUnitFileUEM> getCostUnitFileUEMs() {
		return costUnitFileUEMs;
	}


	public void setCostUnitFileUEMs(List<CostUnitFileUEM> costUnitFileUEMs) {
		this.costUnitFileUEMs = costUnitFileUEMs;
	}


	public Optional<CostUnitFileVDT> getCostUnitFileVDT() {
		return costUnitFileVDT;
	}


	public void setCostUnitFileVDT(CostUnitFileVDT costUnitFileVDT) {
		this.costUnitFileVDT = Optional.ofNullable(costUnitFileVDT);
	}
	
	public List<CostUnitAssignment> getCostUnitAssignment(LocalDate validityFrom, Map<Integer, CostUnitInstitution> kotrInstitutions, Map<String, DTAAccountingCode> mapAccountingCodesCareProviderMethod){
		int[] specialACs = new int[] {0,99,10,20,30,40};
		List<CostUnitAssignment> allKotrAssignments = new ArrayList<CostUnitAssignment>();
		Map<Integer, List<CostUnitFileVKG>> mapByKindOfAssignment = costUnitFileVKGs.stream().collect(Collectors.groupingBy(CostUnitFileVKG::getKindOfAssignment));
        for (Entry<Integer, List<CostUnitFileVKG>> entry : mapByKindOfAssignment.entrySet()) {
        	List<CostUnitAssignment> kotrAssignmentsByKindOfAssignment = new ArrayList<CostUnitAssignment>();
        	List<CostUnitFileVKG> fileVKGs = entry.getValue().stream().sorted(Comparator.comparing(CostUnitFileVKG::getAccountingCode)).collect(Collectors.toList());
        	
        	//*** Alle Datensätze mit korrekten Abrechnungscodes
        	List<CostUnitFileVKG> listVKGsWithCorrectACs =fileVKGs.stream().filter(v -> v.getAccountingCode()!=null).filter(v -> IntStream.of(specialACs).noneMatch(any -> any == v.getAccountingCode().intValue())).collect(Collectors.toList());
        	 for (CostUnitFileVKG costUnitFileVKG : listVKGsWithCorrectACs) {
        		 if(costUnitFileVKG.getAccountingCode()==18 || costUnitFileVKG.getAccountingCode()==60) {
        			 /* 18-Sanitätshaus (Bei neuen Verträgen bzw. Vertragsanpassungen ist eine Umschlüsselung mit dem Abrechnungscode 15 
        			  * vorzunehmen. Der Abrechnungscode 18 wird für Sanitätshäuser zum 31.12.2005 aufgehoben.)
        			  * Betriebshilfe 60 ist keinem SAGS zugeordnet!
        			  */
        			 continue;
        		 }
        		 CostUnitAssignment kotrAssignment = costUnitFileVKG.getCostUnitAssignment(validityFrom, kotrInstitutions);
        		 kotrAssignment.setAccountingCodes(getDtaAccountCodes(mapAccountingCodesCareProviderMethod, new String[] {costUnitFileVKG.getAccountingCode().toString()}));
        		 kotrAssignmentsByKindOfAssignment.add(kotrAssignment);
			}//***
        	         	 
        	 for (CostUnitFileVKG costUnitFileVKG : fileVKGs) {
        		 CostUnitAssignment kotrAssignment = costUnitFileVKG.getCostUnitAssignment(validityFrom, kotrInstitutions);
				if(!listVKGsWithCorrectACs.contains(costUnitFileVKG)) {
					if(costUnitFileVKG.getAccountingCode()== null) {
						kotrAssignment.setAccountingCodes(new ArrayList<DTAAccountingCode>());//00-Sammelschlüssel für alle Leistungsarten
						kotrAssignmentsByKindOfAssignment.add(kotrAssignment);
					} else {
						switch ( costUnitFileVKG.getAccountingCode()) {
							case 0://00-Sammelschlüssel für alle Leistungsarten
								kotrAssignment.setAccountingCodes(new ArrayList<DTAAccountingCode>());//00-Sammelschlüssel für alle Leistungsarten
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
								List<DTAAccountingCode> listAllocatedACs = kotrAssignmentsByKindOfAssignment.stream().filter(v -> v.getAccountingCodes()!=null).map(KotrAssignment::getAccountingCodes).flatMap(List::stream).collect(Collectors.toList());
								List<DTAAccountingCode> listRemainingdACs = mapAccountingCodesCareProviderMethod.values().stream().filter(ac -> !listAllocatedACs.contains(ac)).collect(Collectors.toList());
								kotrAssignment.setAccountingCodes(listRemainingdACs);
								break;
							default:
								break;
						}
						kotrAssignmentsByKindOfAssignment.add(kotrAssignment);
					}
				}
			}
        	 allKotrAssignments.addAll(kotrAssignmentsByKindOfAssignment);
		}
        
        Map<String, CostUnitAssignment> mapGroupedKotrAssignments = new HashMap<>();
        for (CostUnitAssignment currentAssignment : allKotrAssignments) {
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
					throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Gruppierung der Verknüpfungen fehlgeschlagen!");
				}
				boolean exist = existingAssignment.getAccountingCodes().stream().anyMatch(a -> currentAssignment.getAccountingCodes().contains(a));
				if(exist) {
					throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Gruppierung der Verknüpfungen fehlgeschlagen!");
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
	
	private List<DTAAccountingCode> getDtaAccountCodes(Map<String, DTAAccountingCode> mapAccountingCodesCareProviderMethod, String[] searchACs) {
		if (searchACs == null || searchACs.length <= 0) {
			throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Keine ACs übergeben!");
		}
		List<DTAAccountingCode> listAccountingCodes = new ArrayList<>();
		for (String currentAC : searchACs) {
			DTAAccountingCode accountingCode = mapAccountingCodesCareProviderMethod.get(currentAC);
			if (accountingCode == null) {
				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Kein korrekter Abrechnungscode! " + currentAC);
			}
			listAccountingCodes.add(accountingCode);
		}
		return listAccountingCodes;
	}

	/**
	 * Ermittelt anhand den eingelesenen Daten die mögliche Anschrift
	 * 
	 * @return Anschrift
	 * @throws ApplicationException
	 * @throws IOException 
	 */
	public CostUnitAddress getCostUnitAddress() {
		CostUnitAddress addressZip = null;
		CostUnitAddress addressPostCode = null;
		if (costUnitFileNAM != null && costUnitFileNAM.getCostUnitFileANSs() != null && !costUnitFileNAM.getCostUnitFileANSs().isEmpty()) {//NAM-Segment: einmal obligatorisch und Adressen vorhanden
			Date validityFrom = costUnitFileVDT.getValidityFrom() != null ? Date.valueOf(costUnitFileVDT.getValidityFrom()) : null;
			Date validityUntil = costUnitFileVDT.getValidityUntil() != null ? Date.valueOf(costUnitFileVDT.getValidityUntil()) : null;
			for (CostUnitFileANS currentANS : costUnitFileNAM.getCostUnitFileANSs()) {
				CostUnitAddress addressTmp = currentANS.getODAContactAddress(validityFrom, validityUntil);
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
		institution.setActiveIndicator(Boolean.TRUE);//in der Datei befinden sich nur aktuell gültige Institutionen
		institution.setCareProviderMethod(careProviderMethod);//wird gesetzt aus den Informationen aus dem UNB-Segment
		institution.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
		if (costUnitFileVDT != null) {
			institution.setValidityFrom(costUnitFileVDT.getValidityFrom());
			institution.setValidityUntil(costUnitFileVDT.getValidityUntil());
		}

		institution.setFirmName(shortDescription);
		if (costUnitFileNAM != null) {//NAM-Segment: einmal obligatorisch
			String[] names = new String[] {costUnitFileNAM.getName1(), costUnitFileNAM.getName2(), costUnitFileNAM.getName3(), costUnitFileNAM.getName4()};
			String collectedName = Arrays.stream(names).filter(s -> s!=null && !s.trim().isEmpty()).collect(Collectors.joining(" "));
			if(collectedName!=null && !collectedName.isEmpty()) {
				institution.setFirmName(collectedName);
			}
		}
		institution.setInstitutionNumber(institutionCode);
		institution.setVknr(vKNR);
		institution.setCurrentODAContactAddress(getODAContactAddress());
		institution.setShortDescription(shortDescription);
		ODAContactType contactType = FacadeHandler.getMasterDataInfFacadeLocal().findODAContactTypeById(ODAContactType.KOTR_INSTITUTION);
		institution.setODAContactType(contactType);
		institution.setMembership(Integer.valueOf(0));
		return institution;
	}

}
