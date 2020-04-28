package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import costunitimport.model.CostUnitTypeAssignment;

public interface CostUnitTypeAssignmentRepository extends JpaRepository<CostUnitTypeAssignment, Integer> {

}
