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
import costunitimport.dao.CountryRepository;
import costunitimport.dao.FederalStateRepository;
import costunitimport.dao.ZipRepository;

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
	private CountryRepository countryRepository;
	
	@Autowired
	private FederalStateRepository federalStateRepository;

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

	
	public CareProviderMethodRepository getCareProviderMethodRepository() {
		return careProviderMethodRepository;
	}
	
	public ZipRepository getZipRepository() {
		return zipRepository;
	}
	
	public CountryRepository getCountryRepository() {
		return countryRepository;
	}
	
	public FederalStateRepository getFederalStateRepository() {
		return federalStateRepository;
	}
}
