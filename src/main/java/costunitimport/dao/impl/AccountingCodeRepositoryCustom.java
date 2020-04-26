package costunitimport.dao.impl;

import java.util.List;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.DTAAccountingCode;

public interface AccountingCodeRepositoryCustom {
	List<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethod(CareProviderMethod careProviderMethod);
}
