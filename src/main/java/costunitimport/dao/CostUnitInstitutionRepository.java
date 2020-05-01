package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitInstitution;

@Repository
public interface CostUnitInstitutionRepository extends JpaRepository<CostUnitInstitution, Integer> {
	public List<CostUnitInstitution> findByCareProviderMethod(CareProviderMethod careProviderMethod);
}
