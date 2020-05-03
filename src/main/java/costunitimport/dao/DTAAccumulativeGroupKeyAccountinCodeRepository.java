package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import costunitimport.model.sags.DTAAccumulativeGroupKeyAccountinCode;
import costunitimport.model.sags.SAGSAccountingCodePK;

public interface DTAAccumulativeGroupKeyAccountinCodeRepository extends JpaRepository<DTAAccumulativeGroupKeyAccountinCode, SAGSAccountingCodePK> {
	public List<DTAAccumulativeGroupKeyAccountinCode> findByIdSagsId(Integer sagsId);
}
