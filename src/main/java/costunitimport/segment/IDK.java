package costunitimport.segment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.model.ASP_ContactPerson;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.DTACostUnitSeparation;
import costunitimport.model.UEM_Transfer;
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
	
	public List<CostUnitAssignment> buildCostUnitAssignment(LocalDate validityFrom, CareProviderMethod cpm, 
			DTACostUnitSeparation costUnitSeperation){
		List<CostUnitAssignment> assignments = new ArrayList<>();

		/*
		 * Zu den sonsitgen Leistungserbringern 5 - alle Abrechnungscodes beschaffen
		 * (Abrechnungscode identifiziert eine Leistungserbringerart)
		 */
		Map<Integer, DTAAccountingCode> idToAccountoungCode = rFactory.getAccountingCodeRepositoryCustom()
				.findIDToDTAAccountingCodesByCareProviderMethodId(cpm.getId());
		
		/* Jetzt werden die aktualisierten Kasseninstitutionen geladen */
		Map<Integer, CostUnitInstitution> institutions = rFactory.getCostUnitInstitutionRepositoryCustom()
				.findIKToLatestInstituinMapByCareProviderIdAndCostUnitSeparationId(cpm.getId(),
						costUnitSeperation.getId());
		
		List<VKG> vkgs = listVKG.stream().sorted(Comparator.comparing(VKG::getAccountingCode))
				.collect(Collectors.toList());

		for (VKG vkg : vkgs) {
			CostUnitAssignment assignment = vkg.buildCostUnitAssignment(validityFrom, institutions, institutionCode);
			assignments.add(assignment);

			// 99-Sonderschlüssel, gilt für alle in der Kostenträgerdatei nicht aufgeführten
			// Gruppen- und Einzelschlüssel
			if (vkg.getAccountingCode() != null && vkg.getAccountingCode().intValue() == 99) {
				List<Integer> listAllocatedACs = assignments.stream().filter(v -> v.getAccountingCodes() != null).
						map(CostUnitAssignment::getAccountingCodes).flatMap(List::stream).collect(Collectors.toList());
				
				List<DTAAccountingCode> listRemainingdACs = idToAccountoungCode.values().stream().
						filter(ac -> !listAllocatedACs.contains(ac.getAccountingCode())).collect(Collectors.toList());
				
				List<Integer> listRemainingdACsInts = listRemainingdACs.
						stream().map(DTAAccountingCode::getAccountingCode).collect(Collectors.toList());

				assignment.setAccountingCodes(listRemainingdACsInts);
			}
		}
		return assignments;
	}
	
	/**
	 * Ermittelt anhand den eingelesenen Daten die mögliche Anschrift
	 * 
	 * @return Anschrift
	 */
	public List<Address> buildCostUnitAddress() {
		List<Address> addressList = new ArrayList<>();
		
		LocalDate validityFrom = getVDT().getValidityFrom();
		LocalDate validityUntil = getVDT().getValidityUntil();
		
		nam.get().getANSs().forEach(x -> addressList.add(x.buildAddress(validityFrom, validityUntil)));
		return addressList;
	}
	
	public CostUnitInstitution buildCostUnitInstitution(CareProviderMethod careProviderMethod, DTACostUnitSeparation costUnitSeparation) {
		CostUnitInstitution institution = new CostUnitInstitution();
		institution.setCostUnitSeparation(costUnitSeparation.getId());
		institution.setCareProviderMethodId(careProviderMethod.getId());
		institution.setCreationTime(LocalDateTime.now());
		institution.setValidityFrom(getVDT().getValidityFrom());
		institution.setValidityUntil(getVDT().getValidityUntil());
		institution.setFirmName(buildFirmName());
		institution.setInstitutionNumber(institutionCode);
		institution.setContactPersons(buildContactPersons());
		institution.setTransferList(buildTransferList());
		institution.setVknr(vKNR);
		institution.setAddressList(buildCostUnitAddress());
		institution.setShortDescription(description);
		return institution;
	}
	
	private List<UEM_Transfer> buildTransferList() {
		List<UEM_Transfer> transferList = new ArrayList<>(); 
		
		listUEMs.forEach(uem -> transferList.add(uem.buildTransfer()));
		
		return transferList;
	}

	private List<ASP_ContactPerson> buildContactPersons() {
		List<ASP_ContactPerson> contactPersons = new ArrayList<>(); 
		
		for(ASP asp : getASPs()) {
			ASP_ContactPerson person = new ASP_ContactPerson();
			person.setFax(asp.getFax());
			person.setFieldOfActivity(asp.getFieldOfActivity());
			person.setName(asp.getName());
			person.setTelephone(asp.getTelephone());
			contactPersons.add(person);
		}
		return contactPersons;
	}

	private String buildFirmName() {
		String[] names = new String[] { nam.get().getName1(), nam.get().getName2(), nam.get().getName3(),nam.get().getName4() };
		String collectedName = Arrays.stream(names).filter(s -> s != null && !s.trim().isEmpty()).collect(Collectors.joining(" "));
		if (collectedName != null && !collectedName.isEmpty()) {
			return collectedName;
		}
		return description;
	}
}
