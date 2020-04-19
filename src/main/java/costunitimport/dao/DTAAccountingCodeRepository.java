package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.DTAAccountingCode;

@Repository
public interface DTAAccountingCodeRepository extends JpaRepository<DTAAccountingCode, Integer> {
	List<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethod(CareProviderMethod careProviderMethod);
}
