package costunitimport.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import costunitimport.model.CostUnitAssignment;

@Repository
public interface CostUnitAssignmentRepository extends JpaRepository<CostUnitAssignment, Integer> {
	List<CostUnitAssignment> findByInstitutionIdAndValidityFrom(Integer institutionId, LocalDate importFileValidityFrom);
	
	List<CostUnitAssignment> findByTypeAssignmentIdAndParentInstitutionIdAndCareProverMethodIdAndTypeMediumIdAndCostUnitSeparationId(Integer typeAssignmentId, Integer parentInstitutionId, Integer careProverMethodId, Integer typeMediumId, Integer costUnitSeparationId);
}
