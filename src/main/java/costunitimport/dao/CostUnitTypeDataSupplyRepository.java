package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CostUnitTypeDataSupply;

@Repository
public interface CostUnitTypeDataSupplyRepository extends JpaRepository<CostUnitTypeDataSupply, Integer> {

}
