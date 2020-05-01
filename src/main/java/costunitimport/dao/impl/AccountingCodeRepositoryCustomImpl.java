package costunitimport.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import costunitimport.dao.AccountingCodeRepository;
import costunitimport.dao.DTAAccumulativeGroupKeyRepository;
import costunitimport.dao.SAGSDTAAccountingRepository;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.sags.DTAAccumulativeGroupKey;
import costunitimport.model.sags.DTAAccumulativeGroupKeyAccountinCode;

@Repository
public class AccountingCodeRepositoryCustomImpl implements AccountingCodeRepositoryCustom{
	
	 @Autowired
	 DTAAccumulativeGroupKeyRepository sagsRepository;
	 
	 @Autowired
	 SAGSDTAAccountingRepository sagsDTAAccountingCodeRepository;
	 
	 @Autowired
	 AccountingCodeRepository accountingCodeRepository; 
	 
	@Override
	public List<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethod(CareProviderMethod careProviderMethod) {
		List<DTAAccountingCode> codes = new ArrayList<>();

		List<DTAAccumulativeGroupKey> sagsList = sagsRepository.findByCareProviderMethod(careProviderMethod);

		for (DTAAccumulativeGroupKey sags : sagsList) {
			List<DTAAccumulativeGroupKeyAccountinCode> sagsAccountingCodes = sags.getSagsDTAAccountingCode();

			for (DTAAccumulativeGroupKeyAccountinCode sagsDTAAccountingCode : sagsAccountingCodes) {
				Optional<DTAAccountingCode> dtaAccountingCode = accountingCodeRepository
						.findById(sagsDTAAccountingCode.getId().getAccountingId());
				dtaAccountingCode.ifPresent(code -> codes.add(code));
			}
		}
		return codes;
	}
	
}
