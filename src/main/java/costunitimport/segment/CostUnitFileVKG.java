package costunitimport.segment;

import java.time.LocalDate;
import java.util.Map;

import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;

public class CostUnitFileVKG extends CostUnitFileAbstract {

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

	public CostUnitFileVKG(String[] data) {
		super(data);
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
	
	public CostUnitAssignment getCostUnitAssignment(LocalDate validityFrom, Map<Integer, CostUnitInstitution> kotrInstitutions) {
		CostUnitAssignment assignment = new CostUnitAssignment();
		//*** Art der Verknüpfung
		KotrTypeAssignment typeAssignment = FacadeHandler.getMasterDataKotrFacadeLocal().findKotrTypeAssignmentById(kindOfAssignment);
		if(typeAssignment==null) {
			throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Unbekannte Art der Verknuepfung! Id: " + kindOfAssignment);
		}
		assignment.setTypeAssignment(typeAssignment);
		//*** Art der Datenlieferung
		if(kindOfDataSupply!=null) {
			KotrTypeDataSupply typeDataSupply = FacadeHandler.getMasterDataKotrFacadeLocal().findKotrTypeDataSupplyById(kindOfDataSupply);
			if(typeDataSupply==null) {
				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Unbekannte Art der Datenlieferung! Id: " + typeDataSupply);
			}
			assignment.setTypeDataSupply(typeDataSupply);
		}
		//*** Art des Mediums 
		if(kindOfDataMedium!=null) {
			KotrTypeMedium typeMedium = FacadeHandler.getMasterDataKotrFacadeLocal().findKotrTypeMediumById(kindOfDataMedium);
			if(typeMedium==null) {
				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Unbekannte Art des Mediums! Id: " + kindOfDataMedium);
			}
			assignment.setTypeMedium(typeMedium);
		}
		//*** Institut des Verknüpfungspartners
		if(institutionCodeAssignmentPartner!=null) {
			KotrInstitution institutionToSet = kotrInstitutions.get(institutionCodeAssignmentPartner);
			if(institutionToSet==null) {
				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Unbekannte IK des Verknüpfungspartners! IK: " + institutionCodeAssignmentPartner);
			}
			assignment.setInstitutionIdAssignment(institutionToSet.getId());
		}
		//*** Institut der Abrechnungsstelle
		if(institutionCodeAccounting!=null) {
			KotrInstitution institutionToSet = kotrInstitutions.get(institutionCodeAccounting);
			if(institutionToSet==null) {
				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Unbekannte IK des Verknüpfungspartners! IK: " + institutionCodeAccounting);
			}
			assignment.setInstitutionIdAccounting(institutionToSet.getId());
		}
		//***
		
		assignment.setFederalStateClassificationId(federalStateCareProvider);
		assignment.setDistrictId(regionCareProvider);
		String strRateCode = rateCode!=null?rateCode.toString():null;//Tarifkennzeichen ist in der Berschreibung der Kostenträgerdatei nur numerisch
		assignment.setRateCode(strRateCode);
		assignment.setValidityFrom(validityFrom);
		assignment.setValidityUntil(null);
		return assignment;
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
	 * @return Abrechnungscode (Leistungserbringerart)
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
