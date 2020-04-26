package costunitimport.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import costunitimport.dao.AccountingCodeRepository;
import costunitimport.dao.CareProviderMethodRepository;
import costunitimport.dao.CostUnitAddressRepository;
import costunitimport.dao.CostUnitAssignmentRepository;
import costunitimport.dao.CostUnitFileRepository;
import costunitimport.dao.CostUnitInstitutionRepository;
import costunitimport.dao.CostUnitSeparationRepository;
import costunitimport.dao.CostUnitTypeDataSupplyRepository;
import costunitimport.dao.CostUnitTypeMediumRepository;
import costunitimport.dao.CountryRepository;
import costunitimport.dao.FederalStateRepository;
import costunitimport.dao.SAGSRepository;
import costunitimport.dao.ZipRepository;
import costunitimport.dao.ZipTypeRepository;
import costunitimport.dao.impl.AccountingCodeRepositoryCustomImpl;

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
	private ZipRepository zipRepository;
	
	@Autowired
	private ZipTypeRepository zipTypeRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private FederalStateRepository federalStateRepository;
	
	@Autowired
	private CostUnitTypeMediumRepository  costUnitTypeMediumRepository;
	
	@Autowired
	private CostUnitTypeDataSupplyRepository costUnitTypeDataSupplyRepository;
	
	@Autowired
	private SAGSRepository sAGSRepository;
	
	@Autowired
	private AccountingCodeRepositoryCustomImpl accountingCodeRepositoryImpl;
	
	public SAGSRepository getSAGSRepository() {
		return sAGSRepository;
	}
	
	public CostUnitFileRepository getCostUnitFileRepository() {
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
	
	public ZipRepository getZipRepository() {
		return zipRepository;
	}
	
	public ZipTypeRepository getZipTypeRepository() {
		return zipTypeRepository;
	}
	
	public CountryRepository getCountryRepository() {
		return countryRepository;
	}
	
	public FederalStateRepository getFederalStateRepository() {
		return federalStateRepository;
	}
	
	public CostUnitTypeMediumRepository getCostUnitTypeMediumRepository() {
		return costUnitTypeMediumRepository;
	}

	public AccountingCodeRepositoryCustomImpl getAccountingCodeRepositoryImpl() {
		return accountingCodeRepositoryImpl;
	}
}
