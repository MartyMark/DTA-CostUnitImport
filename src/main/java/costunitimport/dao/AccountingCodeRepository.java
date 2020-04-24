package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.DTACareProviderMethod;
import costunitimport.model.DTAAccountingCode;

@Repository
public interface AccountingCodeRepository extends JpaRepository<DTAAccountingCode, Integer> {
	List<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethod(DTACareProviderMethod careProviderMethod);
}
