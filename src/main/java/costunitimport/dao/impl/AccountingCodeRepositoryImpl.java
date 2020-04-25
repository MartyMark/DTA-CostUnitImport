package costunitimport.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import costunitimport.dao.AccountingCodeRepository;
import costunitimport.dao.SAGSDTAAccountingRepository;
import costunitimport.dao.SAGSRepository;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.DTACareProviderMethod;
import costunitimport.model.SAGS;
import costunitimport.model.SAGSDTAAccountingCode;

@Repository
public class AccountingCodeRepositoryImpl {
	
	 @Autowired
	 SAGSRepository sagsRepository;
	 
	 @Autowired
	 SAGSDTAAccountingRepository sagsDTAAccountingCodeRepository;
	 
	 @Autowired
	 AccountingCodeRepository accountingCodeRepository; 
	 
	 public List<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethod(DTACareProviderMethod careProviderMethod){
		 List<DTAAccountingCode> codes = new ArrayList<>();
		 
		 List<SAGS> sagsList = sagsRepository.findByDTACareProviderMethod(careProviderMethod);
		 
		 for(SAGS sags : sagsList) {
			 List<SAGSDTAAccountingCode> sagsAccountingCodes = sags.getSagsDTAAccountingCode(); 
			 
			 for(SAGSDTAAccountingCode sagsDTAAccountingCode : sagsAccountingCodes) {
				 Optional<DTAAccountingCode> dtaAccountingCode = accountingCodeRepository.findById(sagsDTAAccountingCode.getId().getAccountingId());
				 dtaAccountingCode.ifPresent(code -> codes.add(code));
			 }
		 }
		 return codes;
	 }
	
	
}
