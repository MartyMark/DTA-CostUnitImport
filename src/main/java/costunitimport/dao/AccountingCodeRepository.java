package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.AccountingCode;

@Repository
public interface AccountingCodeRepository extends JpaRepository<AccountingCode, Integer> {
	List<AccountingCode> findDTAAccountingCodesByCareProviderMethod(CareProviderMethod careProviderMethod);
}
