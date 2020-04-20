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
import lombok.Getter;

@Getter
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
}
