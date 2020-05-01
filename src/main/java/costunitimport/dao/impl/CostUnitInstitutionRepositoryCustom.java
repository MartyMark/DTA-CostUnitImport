package costunitimport.dao.impl;

import java.util.List;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitInstitution;

public interface CostUnitInstitutionRepositoryCustom {
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethod(CareProviderMethod careProviderMethod);
}
