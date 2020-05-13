package costunitimport.segment;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.CareProviderMethodNotFoundException;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.exception.CostUnitTypeAssigmentNotFoundException;
import costunitimport.exception.CostUnitTypeDataSupplyNotFoundException;
import costunitimport.exception.CostUnitTypeMediumNotFoundException;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.CostUnitTypeAssignment;
import costunitimport.model.CostUnitTypeDataSupply;
import costunitimport.model.CostUnitTypeMedium;
import costunitimport.model.DTAAccountingCode;

/**
 * 
 * VKG+02+100295017+5++07++01++00
 */
public class VKG extends Segment {

	private Integer kindOfAssignment;
	private Integer institutionCodeAssignmentPartner;
	private Integer careProviderGroup;
	private Integer institutionCodeAccounting;
	private Integer kindOfDataSupply;
	private Integer kindOfDataMedium;
	private Integer federalStateCareProvider;
	private Integer regionCareProvider;
	private Integer accountingCode;
	private Integer rateCode;
	
	private final RepositoryFactory rFactory;

	public VKG(String[] data, RepositoryFactory rFactory) {
		super(data);
		this.rFactory = rFactory;
	}

	@Override
	protected void assignData() {
		int position = 1;
		kindOfAssignment = getData(position++, Integer.class);
		institutionCodeAssignmentPartner = getData(position++, Integer.class);
		careProviderGroup = getData(position++, Integer.class);
		institutionCodeAccounting = getData(position++, Integer.class);
		kindOfDataSupply = getData(position++, Integer.class);
		kindOfDataMedium = getData(position++, Integer.class);
		federalStateCareProvider = getData(position++, Integer.class);
		regionCareProvider = getData(position++, Integer.class);
		accountingCode = getData(position++, Integer.class);
		rateCode = getData(position, Integer.class);
	}
	
	/**
	 * Baut das Verknüpfungs-Objekt
	 * @param parentInstitutionCode 
	 */
	public CostUnitAssignment buildCostUnitAssignment(LocalDate validityFrom, Map<Integer, CostUnitInstitution> institutions, Integer parentInstitutionCode) {
		CostUnitAssignment assignment = new CostUnitAssignment();
		
		assignment.setParentInstitutionId(parentInstitutionCode);
		
		//*** Art der Verknüpfung
		CostUnitTypeAssignment typeAssignment = rFactory.getCostUnitTypeAssignmentRepository().findById(kindOfAssignment).orElseThrow(() -> new CostUnitTypeAssigmentNotFoundException(kindOfAssignment));
		assignment.setTypeAssignmentId(typeAssignment.getId());
		
		//*** Art der Datenlieferung
		if(kindOfDataSupply != null) {
			CostUnitTypeDataSupply typeDataSupply = rFactory.getCostUnitTypeDataSupplyRepository().findById(kindOfDataSupply).orElseThrow(() -> new CostUnitTypeDataSupplyNotFoundException(kindOfDataSupply));
			assignment.setTypeDataSupply(typeDataSupply);
		}
		//*** Art des Mediums 
		if(kindOfDataMedium!=null) {
			CostUnitTypeMedium typeMedium = rFactory.getCostUnitTypeMediumRepository().findById(kindOfDataMedium).orElseThrow(() -> new CostUnitTypeMediumNotFoundException(kindOfDataMedium));
			assignment.setTypeMediumId(typeMedium.getId());
		}
		
		//*** Institut des Verknüpfungspartners
		CostUnitInstitution institutionToSet = Optional.ofNullable(institutions.get(institutionCodeAssignmentPartner)).orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionCodeAssignmentPartner));
		assignment.setInstitutionIdAssignment(institutionToSet.getInstitutionNumber());
		
		//*** Institut der Abrechnungsstelle
		if(institutionCodeAccounting!=null) {
			CostUnitInstitution institutionToSetAcc = Optional.ofNullable(institutions.get(institutionCodeAccounting)).orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionCodeAccounting));
			assignment.setInstitutionIdAccounting(institutionToSetAcc.getId());
		}
		
		//*** Leistungserbringergruppe
		if(careProviderGroup != null) {
			CareProviderMethod careProviderMethod = rFactory.getCareProviderMethodRepository().findById(careProviderGroup).orElseThrow(() -> new CareProviderMethodNotFoundException(careProviderGroup));
			assignment.setCareProverMethodId(careProviderMethod.getId());
		}
		
		assignment.setFederalStateClassificationId(federalStateCareProvider);
		assignment.setDistrictId(regionCareProvider);
		String strRateCode = rateCode != null ? rateCode.toString() : null;//Tarifkennzeichen ist in der Berschreibung der Kostenträgerdatei nur numerisch
		assignment.setRateCode(strRateCode);
		assignment.setValidityFrom(validityFrom);
		assignment.setValidityUntil(null);
		assignment.setAccountingCodes(findAccountingCodes(validityFrom, institutions));
		return assignment;
	}

	private List<Integer> findAccountingCodes(LocalDate validityFrom, Map<Integer, CostUnitInstitution> institutions) {
		/* Das sind die Ids der Obergruppen der Abrechnungscodes bspw : 
		   00 Sammelschlüssel für alle Leistungsarten , 10 Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)*/
		List<Integer> groupAccountCodes = DTAAccountingCode.getGroupAccountingCodes(); 
		
		if(accountingCode == null) {
			return Collections.emptyList();//00-Sammelschlüssel für alle Leistungsarten
		}
		
		/* Handelt sich um einen Gruppenschlüssel */
		if(groupAccountCodes.contains(accountingCode)) {
			switch (accountingCode) {
			case 0://00-Sammelschlüssel für alle Leistungsarten
				return Collections.emptyList();//00-Sammelschlüssel für alle Leistungsarten
			case 10://10-Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)
				return DTAAccountingCode.getHimiCodes();
			case 20://20-Gruppenschlüssel Heilmittelerbringer (Schlüssel 21-29)
				return DTAAccountingCode.getHeimiCodes();
			case 30://30-Gruppenschlüssel Häusliche Krankenpflege (Schlüssel 31-34)
				return DTAAccountingCode.getHpfCodes();
			case 40://40-Gruppenschlüssel Krankentransportleistungen (Schlüssel 41-49)
				return DTAAccountingCode.getTransportCodes();
			default:
				break;
			}
		}
		return List.of(accountingCode);
	}

	/**
	 * Art der Verknüpfung<br>
	 * Schlüssel Art der Verknüpfung
	 * 
	 * @return Art der Verknüpfung<br><br>
	 * 01 - Verweis vom IK der Versichertenkarte zum Kostenträger<br><br>
	 * 
	 * 02 - Verweis auf eine Datenannahmestelle (ohne Entschlüsselungsbefugnis)
	 * 	    Schlüssel ist nur gültig in Verbindung mit dem Schlüssel "Art der Datenlieferung"<br><br>
	 * 03 - Verweis auf eine Datenannahmestelle (mit Entschlüsselungsbefugnis)<br>
	 *      Schlüssel ist nur gültig in Verbindung mit dem Schlüssel 07 "Art der Datenlieferung"<br><br>
	 * 09 - Verweis aufeine Papierannahmestelle
	 */
	public Integer getKindOfAssignment() {
		return kindOfAssignment;
	}

	/**
	 * IK des Verknüpfungspartners<br>
	 * Institutionskennzeichen
	 * 
	 * @return IK des Verknüpfungspartners
	 */
	public Integer getInstitutionCodeAssignmentPartner() {
		return institutionCodeAssignmentPartner;
	}

	/**
	 * Leistungserbringergruppe<br>
	 * Schlüssel LE-Gruppe
	 * 
	 * @return Leistungserbringergruppe
	 */
	public Integer getCareProviderGroup() {
		return careProviderGroup;
	}

	/**
	 * IK der Abrechnungsstelle<br>
	 * IK der Abrechnungsstelle, für welche dieser Verweis gültig ist.<br>
	 * Fehlt dieser Eintrag, ist der Verweis für alle Stellen gültig.
	 * 
	 * @return IK der Abrechnungsstelle
	 */
	public Integer getInstitutionCodeAccounting() {
		return institutionCodeAccounting;
	}

	/**
	 * Art der Datenlieferung<br>
	 * Schlüssel Art der Datenlieferung
	 * 
	 * @return Art der Datenlieferung
	 */
	public Integer getKindOfDataSupply() {
		return kindOfDataSupply;
	}

	/**
	 * Art des Übermittlungsmediums<br>
	 * Schlüssel Übermittlungsmedium
	 * 
	 * @return Art des Übermittlungsmediums
	 */
	public Integer getKindOfDataMedium() {
		return kindOfDataMedium;
	}

	/**
	 * Standort des Leistungserbringers nach Bundesland
	 * 
	 * @return Standort des Leistungserbringers nach Bundesland
	 */
	public Integer getFederalStateCareProvider() {
		return federalStateCareProvider;
	}

	/**
	 * Standort des Leistungserbringers nach KV-Bezirk
	 * 
	 * @return Standort des Leistungserbringers nach KV-Bezirk
	 */
	public Integer getRegionCareProvider() {
		return regionCareProvider;
	}

	/**
	 * Abrechnungscode (Leistungserbringerart)<br>
	 * Schlüssel Abrechnungscode
	 * 
	 * @return Abrechnungscode (Leistungserbringerart)<br>
	 * 00 - Sammelschlüssel für alle Leistungsarten<br>
	 * 99 - Sonderschlüssel, gilt für alle in der Kostenträgerdatei nicht aufgeführten Gruppen- und Einzelschlüssel<br><br>
	 * 
	 * 10- Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)<br>
	 * 		11 - Apotheke (mit gesonderter Zulassung nach § 126 SGB V)<br>
	 * 		12 - Augenoptiker<br>
	 * 		13 - Augenarzt<br>
	 * 		etc....
	 */
	public Integer getAccountingCode() {
		return accountingCode;
	}

	/**
	 * Tarifkennzeichen<br>
	 * Schlüssel Tarifkennzeichen
	 * 
	 * @return Tarifkennzeichen
	 */
	public Integer getRateCode() {
		return rateCode;
	}
}
