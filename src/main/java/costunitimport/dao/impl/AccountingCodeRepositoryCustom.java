package costunitimport.dao.impl;

import java.util.Map;
import java.util.Set;

import costunitimport.model.DTAAccountingCode;

public interface AccountingCodeRepositoryCustom {
	Set<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethodId(Integer careProviderMethodId);
	
	Map<Integer, DTAAccountingCode> findIDToDTAAccountingCodesByCareProviderMethodId(Integer careProviderMethodId);
}
