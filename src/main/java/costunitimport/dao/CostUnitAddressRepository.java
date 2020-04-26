package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.address.Address;

@Repository
public interface CostUnitAddressRepository extends JpaRepository<Address, Integer> {

}
