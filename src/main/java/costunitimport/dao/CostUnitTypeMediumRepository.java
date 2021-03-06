package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CostUnitTypeMedium;

@Repository
public interface CostUnitTypeMediumRepository extends JpaRepository<CostUnitTypeMedium, Integer> {

}
