package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CostUnitInstitution;

@Repository
public interface CostUnitInstitutionRepository extends JpaRepository<CostUnitInstitution, Integer> {
	public List<CostUnitInstitution> findByCareProviderMethodIdAndCostUnitSeparationId(Integer careProviderMethodId, Integer costUnitSeparationId);
	
	public List<CostUnitInstitution> findByInstitutionNumberAndCostUnitSeparationId(Integer institutionNumber, Integer costUnitSeparationId);
	
	public List<CostUnitInstitution> findByInstitutionNumber(Integer institutionNumber);
}
