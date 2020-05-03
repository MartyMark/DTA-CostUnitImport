package costunitimport.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import costunitimport.dao.AccountingCodeRepository;
import costunitimport.dao.DTAAccumulativeGroupKeyAccountinCodeRepository;
import costunitimport.dao.DTAAccumulativeGroupKeyRepository;
import costunitimport.dao.SAGSDTAAccountingRepository;
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
	 
	 @Autowired
	 DTAAccumulativeGroupKeyAccountinCodeRepository accumulativeGroupKeyAccountinCodeRepository;
	 
	@Override
	public Set<DTAAccountingCode> findDTAAccountingCodesByCareProviderMethodId(Integer careProviderMethodId) {
		List<DTAAccumulativeGroupKey> sagsList = sagsRepository.findByCareProviderMethodId(careProviderMethodId);
		
		List<DTAAccumulativeGroupKeyAccountinCode> sagsAccountingPKList = new ArrayList<>(); 
		for (DTAAccumulativeGroupKey sags : sagsList) {
			sagsAccountingPKList.addAll(accumulativeGroupKeyAccountinCodeRepository.findByIdSagsId(sags.getSagsId()));
		}
		
		Set<DTAAccountingCode> codes = new HashSet<>();
		for(DTAAccumulativeGroupKeyAccountinCode sagsAccountingCodePK : sagsAccountingPKList) {
			codes.add(accountingCodeRepository.findById(sagsAccountingCodePK.getId().getAccountingId()).orElseThrow());
		}
		return codes;
	}

	@Override
	public Map<Integer, DTAAccountingCode> findIDToDTAAccountingCodesByCareProviderMethodId(Integer careProviderMethodId) {
		Set<DTAAccountingCode> accountingCodes = findDTAAccountingCodesByCareProviderMethodId(careProviderMethodId);
		
		//Abrechnungsocde - Leistungserbingerart
		return accountingCodes.stream().distinct().collect(Collectors.toMap(DTAAccountingCode::getAccountingCode, Function.identity()));
	}
	
}
