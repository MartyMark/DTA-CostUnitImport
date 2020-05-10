package costunitimport.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTACostUnitSeparation;

@Service
public class CostUnitServiceP302 {
	
	@Autowired
	private RepositoryFactory rFactory;
	
	public Optional<CostUnitInstitution> findCostUnitByIk(Integer institutionNumber){
		return this.rFactory.getCostUnitInstitutionRepository().findByInstitutionNumber(institutionNumber);
	}
	
	public Optional<CostUnitInstitution> findDAVByIkAndCostUnitSeperationAndAssignmentType(Integer institutionnumber, DTACostUnitSeparation costUnitSeparation, Integer assignmentType) {
		CostUnitInstitution institution = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionByInstitutionNumberAndCostUnitSeparationId(institutionnumber, costUnitSeparation.getId()).orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionnumber));
		
		
		//Dann noch nach AccountingCode prüfen
		
		return rFactory.getCostUnitAssignmentRepository().findDAVByIkAndValidityFromAndCostUnitSeperationAndAssignmentType(institutionnumber, validityFrom, costUnitSeparation, assignmentType);
	}
}
