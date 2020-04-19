package costunitimport.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import costunitimport.model.CostUnitAssignment;

public interface CostUnitAssignmentRepository extends JpaRepository<CostUnitAssignment, Integer> {
	List<CostUnitAssignment> findCostUnitAssignmentsByCostUnitInstitutionIdAndValidityDate(Integer institutionId, LocalDate importFileValidityFrom);
}
