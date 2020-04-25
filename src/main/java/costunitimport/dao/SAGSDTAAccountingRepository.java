package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.SAGSDTAAccountingCode;

@Repository
public interface SAGSDTAAccountingRepository extends JpaRepository<SAGSDTAAccountingCode, Integer> {
	
}
