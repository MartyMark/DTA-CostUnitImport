package costunitimport.dao.impl;

import java.util.List;
import java.util.Map;

import costunitimport.model.CostUnitInstitution;

public interface CostUnitInstitutionRepositoryCustom {
	
	/**
	 * //TODO nach Kassenart trennen + Latest
	 * Beschafft sich zu einem Leistungsverfahren alle gültigen (latest) Kasseninstitutionen.
	 * 
	 * @param careProviderMethodId Leistungsverfahren-ID
	 */
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethodId(Integer careProviderMethodId);
	
	/**
	 * Beschafft sich zu einem Leistungsverfahren alle gültigen (latest) Kasseninstitutionen und mappt dann die jeweilige IK zu der Kasseinstitution
	 * 
	 * @param careProviderMethodId Leistungsverfahren-ID
	 */
	public Map<Integer, CostUnitInstitution> findIKToLatestInstituinMapByCareProviderId(Integer careProviderMethodId);
}
