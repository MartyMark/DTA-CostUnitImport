package costunitimport.dao.impl;

import java.util.List;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.DTAAccountingCode;

public interface AcountingCodeRepositoryCustom {
	List<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethod(CareProviderMethod careProviderMethod);
}
