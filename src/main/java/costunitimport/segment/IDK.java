package costunitimport.segment;

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
import costunitimport.exception.CostUnitAssigmentGroupingException;
import costunitimport.model.AddressType;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.DTACostUnitSeparation;
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
	private List<KTO> listKTOs = new ArrayList<>();
	private List<ASP> listASPs = new ArrayList<>();
	private List<UEM> listUEMs = new ArrayList<>();
	
	public IDK(String[] data) {
		super(data);
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


	public List<VKG> getVKGs() {
		return listVKG;
	}
	
	public void addVKG(VKG vkg) {
		listVKG.add(vkg);
	}

	public FKT getFKT() {
		return Objects.requireNonNull(fkt);
	}


	public void setFKT(FKT fkt) {
		this.fkt =fkt;
	}


	public List<KTO> getKTOs() {
		return listKTOs;
	}
	
	public void addKTO(KTO kto) {
		listKTOs.add(kto);
	}

	public Optional<NAM> getNAM() {
		return nam;
	}


	public void setNAM(NAM nam) {
		this.nam = Optional.ofNullable(nam);
	}


	public List<ASP> getASPs() {
		return listASPs;
	}
	
	public void addASP(ASP asp) {
		listASPs.add(asp);
	}

	public List<UEM> getUEMs() {
		return listUEMs;
	}
	
	public void addUEM(UEM uem) {
		listUEMs.add(uem);
	}

	public VDT getVDT() {
		return Objects.requireNonNull(vdt);
	}


	public void setVDT(VDT vdt) {
		this.vdt = vdt;
	}
	
	public List<CostUnitAssignment> getCostUnitAssignment(LocalDate validityFrom, Map<Integer, CostUnitInstitution> institutions, Map<Integer, DTAAccountingCode> mapAccountingCodesCareProviderMethod){
		List<CostUnitAssignment> allAssignments = new ArrayList<>();
		
		/* Mappt die VKGs nach Schlüssel Art der Verknüpfung. Bswp -> 02 - Verweis auf eine Datenannahmestelle*/
		Map<Integer, List<VKG>> mapByKindOfAssignment = listVKG.stream().collect(Collectors.groupingBy(VKG::getKindOfAssignment));
        
		/* Iteriert durch jede Schlüsselart der Verknüpfung*/
		for (Entry<Integer, List<VKG>> entry : mapByKindOfAssignment.entrySet()) {
        	List<CostUnitAssignment> assignmentsByKindOfAssignment = new ArrayList<>();
        	
        	/* VKGs werden aufsteiged nach Leistungserbringerschlüssel sortiert, damit schon alle Abrechnungscodes bearbeitet worden sind, wenn eine
        	 * Verknüpfung mit dem Abrechnungscode 99 eintrifft */
        	List<VKG> vkgs = entry.getValue().stream().sorted(Comparator.comparing(VKG::getAccountingCode)).collect(Collectors.toList());
        	
        	for(VKG vkg : vkgs) {
				CostUnitAssignment assignment = vkg.buildCostUnitAssignment(validityFrom, institutions);
        		assignmentsByKindOfAssignment.add(assignment);
        		
        		//99-Sonderschlüssel, gilt für alle in der Kostenträgerdatei nicht aufgeführten Gruppen- und Einzelschlüssel
        		if(vkg.getAccountingCode() != null && vkg.getAccountingCode().intValue() == 99) {
					List<DTAAccountingCode> listAllocatedACs = assignmentsByKindOfAssignment.stream().filter(v -> v.getAccountingCodes() != null)
							.map(CostUnitAssignment::getAccountingCodes).flatMap(List::stream).collect(Collectors.toList());
					List<DTAAccountingCode> listRemainingdACs = mapAccountingCodesCareProviderMethod.values().stream().filter(ac -> !listAllocatedACs.contains(ac))
							.collect(Collectors.toList());
					assignment.setAccountingCodes(listRemainingdACs);
        		}
        	}
			allAssignments.addAll(assignmentsByKindOfAssignment);
		}
		
		Map<String, CostUnitAssignment> groupedAssignments = new HashMap<>();
		for (CostUnitAssignment currentAssignment : allAssignments) {
			String key = currentAssignment.getCompareKey();//Man könnte auch die Equals überschreiben und dann mit einem Set arbeiten

			CostUnitAssignment existingAssignment = groupedAssignments.get(key);
			
			/* Hier werden die Verknüpfungen gemappt 
			 * Somit wird aus :
			 * VKG+03+101317004+5++07++++56
			 * VKG+03+101317004+5++07++++57
			 * 
			 * diese Verknüpfung :			 
			 * VKG+03+101317004+5++07++++56,57
			 * 
			 * Der Comparekey bezieht sich auf alle Attribute bis auf die Abrechungscodes
			 * Sind zwei Datensätze gleich werden die Abrechnungscodes nach Verknüpfung gruppiert
			 * */
			if (existingAssignment == null) {
				groupedAssignments.put(key, currentAssignment);
			} else if (existingAssignment.getAccountingCodes() != null) {
				if (currentAssignment.getAccountingCodes() == null) {
					throw new CostUnitAssigmentGroupingException();
				}
				boolean exist = existingAssignment.getAccountingCodes().stream().anyMatch(a -> currentAssignment.getAccountingCodes().contains(a));
				if (exist) {
					throw new CostUnitAssigmentGroupingException();
				}
				existingAssignment.getAccountingCodes().addAll(currentAssignment.getAccountingCodes());
			}
		}
		
		CostUnitInstitution currentInstitution = institutions.get(getInstitutionCode());
		
		//Jede Verknüpfung erhält die InstitutionsId von der sie referenziert wird
		groupedAssignments.values().forEach(x -> x.setParentInstitutionId(currentInstitution.getId()));
		
		return groupedAssignments.values().stream().collect(Collectors.toList());
	}
	
	/**
	 * Ermittelt anhand den eingelesenen Daten die mögliche Anschrift
	 * 
	 * @return Anschrift
	 */
	public Optional<Address> buildCostUnitAddress() {
		Address addressZip = null;
		Address addressPostCode = null;
		if (nam.isPresent() && !nam.get().getANSs().isEmpty()) {//NAM-Segment: einmal obligatorisch und Adressen vorhanden
			LocalDate validityFrom = getVDT().getValidityFrom();
			LocalDate validityUntil = getVDT().getValidityUntil();
			for (ANS ans : nam.get().getANSs()) {
				Address addressTmp = ans.buildAddress(validityFrom, validityUntil);
				if (ans.getKindOfAddress().intValue() == AddressType.STREET.getId()) { //Hausanschrift
					addressZip  = addressTmp;
				} else if (ans.getKindOfAddress().intValue() == AddressType.MAIL_BOX.getId()) { //Postfachanschrift
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
	
	public CostUnitInstitution buildCostUnitInstitution(CareProviderMethod careProviderMethod, DTACostUnitSeparation costUnitSeparation) {
		CostUnitInstitution institution = new CostUnitInstitution();
		institution.setCostUnitSeparation(costUnitSeparation.getId());
		institution.setCareProviderMethodId(careProviderMethod.getId());//wird gesetzt aus den Informationen aus dem UNB-Segment
		institution.setCreationTime(LocalDateTime.now());
		institution.setValidityFrom(getVDT().getValidityFrom());
		institution.setValidityUntil(getVDT().getValidityUntil());
		institution.setFirmName(buildFirmName());
		institution.setInstitutionNumber(institutionCode);
		institution.setVknr(vKNR);
		institution.setAddress(buildCostUnitAddress().orElse(null));
		institution.setShortDescription(description);
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
