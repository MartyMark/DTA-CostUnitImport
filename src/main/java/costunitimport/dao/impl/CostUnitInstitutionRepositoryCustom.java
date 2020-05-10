package costunitimport.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import costunitimport.model.CostUnitInstitution;

public interface CostUnitInstitutionRepositoryCustom {
	
	/**
	 * Beschafft sich zu einem Leistungsverfahren und einer Kassenart alle gültigen (latest) Kasseninstitutionen.
	 * 
	 * @param careProviderMethodId Leistungsverfahren-ID
	 */
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethodIdAndCostUnitSeparationId(Integer careProviderMethodId, Integer costUnitSeparationId);
	
	/**
	 * Beschafft sich zu einem Leistungsverfahren und einer Kassenart alle gültigen (latest) Kasseninstitutionen und mappt dann die jeweilige IK zu der Kasseinstitution
	 * 
	 * @param careProviderMethodId Leistungsverfahren-ID
	 */
	public Map<Integer, CostUnitInstitution> findIKToLatestInstituinMapByCareProviderIdAndCostUnitSeparationId(Integer careProviderMethodId, Integer costUnitSeparationId);

	/**
	 * Beschafft sich zu einem Institutionsnummer und einer Kassenart die gültige (latest) Kasseninstitution.
	 * 
	 * @param careProviderMethodId Leistungsverfahren-ID
	 */
	public Optional<CostUnitInstitution> findLatestCostUnitInstitutionByInstitutionNumberAndCostUnitSeparationId(Integer institutionNumber, Integer costUnitSeparationId);

}
