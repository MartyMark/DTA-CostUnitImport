package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import costunitimport.model.FederalState;

public interface FederalStateRepository extends JpaRepository<FederalState, Integer> {

}
