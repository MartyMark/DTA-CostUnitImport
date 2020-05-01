package costunitimport.dao.impl;

import java.util.List;

import costunitimport.model.CostUnitInstitution;

public interface CostUnitInstitutionRepositoryCustom {
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethodId(Integer careProviderMethodId);
}
