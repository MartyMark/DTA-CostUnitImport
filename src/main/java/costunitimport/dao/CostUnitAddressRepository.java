package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CostUnitAddress;

@Repository
public interface CostUnitAddressRepository extends JpaRepository<CostUnitAddress, Integer> {

}
