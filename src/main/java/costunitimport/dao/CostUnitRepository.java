package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import costunitimport.model.CostUnit;

@Repository
public interface CostUnitRepository extends JpaRepository<CostUnit, Integer> {

}
