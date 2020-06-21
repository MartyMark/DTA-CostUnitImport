package costunitimport.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import costunitimport.dao.AccountingCodeRepository;
import costunitimport.dao.CareProviderMethodRepository;
import costunitimport.dao.CharSetRepoistory;
import costunitimport.dao.CostUnitAddressRepository;
import costunitimport.dao.CostUnitAssignmentRepository;
import costunitimport.dao.CostUnitFileRepository;
import costunitimport.dao.CostUnitInstitutionRepository;
import costunitimport.dao.CostUnitSeparationRepository;
import costunitimport.dao.CostUnitTypeAssignmentRepository;
import costunitimport.dao.CostUnitTypeDataSupplyRepository;
import costunitimport.dao.CostUnitTypeMediumRepository;
import costunitimport.dao.DTAAccumulativeGroupKeyAccountinCodeRepository;
import costunitimport.dao.DTAAccumulativeGroupKeyRepository;
import costunitimport.dao.TransferParameterRepository;
import costunitimport.dao.impl.AccountingCodeRepositoryCustom;
import costunitimport.dao.impl.CostUnitInstitutionRepositoryCustom;

@Component
public class RepositoryFactory {
	
	@Autowired
	private CostUnitFileRepository costUnitFileRepository;
	
	@Autowired
	private CostUnitInstitutionRepository costUnitInstitutionRepository;
	
	@Autowired
	private CostUnitAddressRepository costUnitAddressRepository;
	
	@Autowired
	private AccountingCodeRepository accountingCodeRepository;
	
	@Autowired
	private CostUnitAssignmentRepository costUnitAssignmentRepository;
	
	@Autowired
	private CostUnitSeparationRepository costUnitSeparationRepository;
	
	@Autowired
	private CareProviderMethodRepository careProviderMethodRepository;
	
	@Autowired
	private CostUnitTypeMediumRepository  costUnitTypeMediumRepository;
	
	@Autowired
	private CostUnitTypeDataSupplyRepository costUnitTypeDataSupplyRepository;
	
	@Autowired
	private CostUnitTypeAssignmentRepository costUnitTypeAssignmentRepository;
	
	@Autowired
	private DTAAccumulativeGroupKeyRepository sAGSRepository;
	
	@Autowired
	private AccountingCodeRepositoryCustom accountingCodeRepositoryCustom;
	
	@Autowired
	private DTAAccumulativeGroupKeyAccountinCodeRepository accumulativeGroupKeyAccountingCodeRepository;
	
	@Autowired
	private CostUnitInstitutionRepositoryCustom costUnitInstitutionRepositoryCustom;
	
	@Autowired
	private TransferParameterRepository transferParameterRepository;
	
	@Autowired
	private CharSetRepoistory charSetRepoistory;
	
	public DTAAccumulativeGroupKeyRepository getSAGSRepository() {
		return sAGSRepository;
	}
	
	public CostUnitFileRepository getFileRepository() {
		return costUnitFileRepository;
	}
	
	public CostUnitInstitutionRepository getCostUnitInstitutionRepository() {
		return costUnitInstitutionRepository;
	}
	
	public CostUnitAddressRepository getCostUnitAddressRepository() {
		return costUnitAddressRepository;
	}
	
	public AccountingCodeRepository getAccountingCodeRepository() {
		return accountingCodeRepository;
	}
	
	public CostUnitAssignmentRepository getCostUnitAssignmentRepository() {
		return costUnitAssignmentRepository;
	}
	
	public CostUnitSeparationRepository getCostUnitSeparationRepository() {
		return costUnitSeparationRepository;
	}
	
	public CostUnitTypeDataSupplyRepository getCostUnitTypeDataSupplyRepository() {
		return costUnitTypeDataSupplyRepository;
	}
	
	public CareProviderMethodRepository getCareProviderMethodRepository() {
		return careProviderMethodRepository;
	}
	
	public CostUnitTypeMediumRepository getCostUnitTypeMediumRepository() {
		return costUnitTypeMediumRepository;
	}

	public AccountingCodeRepositoryCustom getAccountingCodeRepositoryCustom() {
		return accountingCodeRepositoryCustom;
	}
	
	public CostUnitTypeAssignmentRepository getCostUnitTypeAssignmentRepository() {
		return costUnitTypeAssignmentRepository;
	}
	
	public CostUnitInstitutionRepositoryCustom getCostUnitInstitutionRepositoryCustom() {
		return costUnitInstitutionRepositoryCustom;
	}

	public DTAAccumulativeGroupKeyAccountinCodeRepository getAccumulativeGroupKeyAccountingCodeRepository() {
		return accumulativeGroupKeyAccountingCodeRepository;
	}

	public TransferParameterRepository getTransferParameterRepository() {
		return transferParameterRepository;
	}

	public CharSetRepoistory getCharSetRepoistory() {
		return charSetRepoistory;
	}
}
