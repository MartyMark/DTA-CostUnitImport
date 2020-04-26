package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.address.FederalState;

@Repository
public interface FederalStateRepository extends JpaRepository<FederalState, Integer> {

}
