package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitInstitution;

public interface CostUnitInstitutionRepository extends JpaRepository<CostUnitInstitution, Integer> {
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethod(CareProviderMethod careProviderMethod);
}
