package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CostUnitInstitution;
import costunitimport.model.CareProviderMethod;

@Repository
public interface CostUnitInstitutionRepository extends JpaRepository<CostUnitInstitution, Integer> {
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethod(CareProviderMethod careProviderMethod);
}
