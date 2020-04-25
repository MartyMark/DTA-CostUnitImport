package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.DTAAccountingCode;

@Repository
public interface AccountingCodeRepository extends JpaRepository<DTAAccountingCode, Integer> {

}
