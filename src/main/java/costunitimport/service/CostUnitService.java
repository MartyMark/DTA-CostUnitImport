package costunitimport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;

@Service
public class CostUnitService {
	
	@Autowired
	private RepositoryFactory rFactory;
	
	public Optional<CostUnitInstitution> findCostUnitInstitution(Integer careProviderId, Integer institutionnumber, Integer accountingCode) throws Exception {
		CostUnitInstitution parentInstitution = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionByInstitutionNumber(institutionnumber).orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionnumber));
		
		List<CostUnitAssignment> subAssignments = rFactory.getCostUnitAssignmentRepository().findByParentInstitutionIdAndCareProverMethodId(parentInstitution.getInstitutionNumber(), careProviderId);
		
		List<CostUnitAssignment> filterdSubAssignments = filterByAccountingCode(subAssignments, accountingCode);
		
		if(filterdSubAssignments.isEmpty()) {
			return Optional.ofNullable(parentInstitution);
		}
		
		if(filterdSubAssignments.size() > 1) {
			throw new Exception();
		}
		CostUnitAssignment ins = filterdSubAssignments.get(0);
		
		return findCostUnitInstitution(careProviderId, ins.getInstitutionIdAssignment(), accountingCode);
	}

	private List<CostUnitAssignment> filterByAccountingCode(List<CostUnitAssignment> subAssignments, Integer accountingCode) {
		List<CostUnitAssignment> filterdSubAssignments = new ArrayList<>();
		
		for(CostUnitAssignment assignment : subAssignments) {
			
			List<Integer> accountingCodes = assignment.getAccountingCodes().stream().map(DTAAccountingCode::getAccountingCode).collect(Collectors.toList());
			
			if(assignment.getAccountingCodes().isEmpty() || accountingCodes.contains(accountingCode)) {
				filterdSubAssignments.add(assignment);
			}
		}
		return filterdSubAssignments;
	}
}
