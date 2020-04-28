package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.sags.DTAAccumulativeGroupKeyAccountinCode;

@Repository
public interface SAGSDTAAccountingRepository extends JpaRepository<DTAAccumulativeGroupKeyAccountinCode, Integer> {
	
}
