package costunitimport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.DTAAccountingCode;

@Service
public class CostUnitServiceP302 {
	
	@Autowired
	private RepositoryFactory rFactory;
	
	public Optional<CostUnitInstitution> findCostUnitByIk(Integer institutionNumber){
		return this.rFactory.getCostUnitInstitutionRepository().findByInstitutionNumber(institutionNumber);
	}
	
	public Optional<CostUnitInstitution> findDAV(Integer institutionnumber, Integer typeAssignment, Integer typeMediumId, Integer accountingCode) throws Exception {
		CostUnitInstitution parentInstitution = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionByInstitutionNumber(institutionnumber).orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionnumber));
		
		List<CostUnitAssignment> subAssignments = rFactory.getCostUnitAssignmentRepository().findByTypeAssignmentIdAndParentInstitutionIdAndCareProverMethodIdAndTypeMediumId(typeAssignment, parentInstitution.getInstitutionNumber(), CareProviderMethod.P_302, typeMediumId);
		
		List<CostUnitAssignment> filterdSubAssignments = filterByAccountingCode(subAssignments, accountingCode);
		
		if(filterdSubAssignments.isEmpty()) {
			return Optional.ofNullable(parentInstitution);
		}
		
		if(filterdSubAssignments.size() > 1) {
			throw new Exception();
		}
		CostUnitAssignment ins = filterdSubAssignments.get(0);
		
		return findDAV(ins.getInstitutionIdAssignment(), typeAssignment, typeMediumId, accountingCode);
	}
	
	public Optional<CostUnitInstitution> findDAV(Integer institutionnumber, Integer costUnitSeparationId, Integer typeAssignment, Integer typeMediumId, Integer accountingCode) throws Exception {
		CostUnitInstitution parentInstitution = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionByInstitutionNumberAndCostUnitSeparationId(institutionnumber, costUnitSeparationId).orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionnumber));
		
		List<CostUnitAssignment> subAssignments = rFactory.getCostUnitAssignmentRepository().findByTypeAssignmentIdAndParentInstitutionIdAndCareProverMethodIdAndTypeMediumIdAndCostUnitSeparationId(typeAssignment, parentInstitution.getInstitutionNumber(), CareProviderMethod.P_302, typeMediumId, costUnitSeparationId);
		
		List<CostUnitAssignment> filterdSubAssignments = filterByAccountingCode(subAssignments, accountingCode);
		
		if(filterdSubAssignments.isEmpty()) {
			return Optional.ofNullable(parentInstitution);
		}
		
		if(filterdSubAssignments.size() > 1) {
			throw new Exception();
		}
		CostUnitAssignment ins = filterdSubAssignments.get(0);
		
		return findDAV(ins.getInstitutionIdAssignment(), costUnitSeparationId, typeAssignment, typeMediumId, accountingCode);
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
