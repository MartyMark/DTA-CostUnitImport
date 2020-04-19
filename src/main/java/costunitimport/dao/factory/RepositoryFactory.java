package costunitimport.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import costunitimport.dao.CostUnitAddressRepository;
import costunitimport.dao.CostUnitAssignmentRepository;
import costunitimport.dao.CostUnitFileRepository;
import costunitimport.dao.CostUnitInstitutionRepository;
import costunitimport.dao.CostUnitRepository;
import costunitimport.dao.DTAAccountingCodeRepository;
import lombok.Getter;

@Getter
@Component
public class RepositoryFactory {
	@Autowired
	private CostUnitRepository costUnitRepository;
	
	@Autowired
	private CostUnitFileRepository costUnitFileRepository;
	
	@Autowired
	private CostUnitInstitutionRepository costUnitInstitutionRepository;
	
	@Autowired
	private CostUnitAddressRepository costUnitAddressRepository;
	
	@Autowired
	private DTAAccountingCodeRepository accountingCodeRepository;
	
	@Autowired
	private CostUnitAssignmentRepository costUnitAssignmentRepository;
}
