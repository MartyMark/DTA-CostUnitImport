package costunitimport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.CostUnitTypeAssignment;
import costunitimport.model.response.AddressData;

@Service
public class CostUnitService {
	
	@Autowired
	private RepositoryFactory rFactory;
	
	public List<AddressData> findDAV(Integer careProviderId, Integer institutionnumber, Integer accountingCode) throws Exception {
		CostUnitInstitution parentInstitution = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionByInstitutionNumber(institutionnumber).orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionnumber));
		
		List<CostUnitAssignment> subAssignments = rFactory.getCostUnitAssignmentRepository().findByParentInstitutionIdAndCareProverMethodId(parentInstitution.getInstitutionNumber(), careProviderId);
		
		List<CostUnitAssignment> filterdSubAssignments = filterByAccountingCode(subAssignments, accountingCode);
		
		List<Integer> institutionsnumbers = filterdSubAssignments.stream().map(CostUnitAssignment::getInstitutionIdAssignment).collect(Collectors.toList());
		
		/* Wenn die IDK-Institutionsnummer im VKG Segment vorhanden ist, findet eine Selbstreferenzierung statt. 
		 * Die Jetzt vorhanden VKGs stellen die entgültigen Datenannahmestellen dar */
		if(institutionsnumbers.contains(parentInstitution.getInstitutionNumber())) {
			return convertToCostUnitAddressData(filterdSubAssignments);
		}
		
		CostUnitAssignment ins = filterdSubAssignments.get(0);
		
		return findDAV(careProviderId, ins.getInstitutionIdAssignment(), accountingCode);
	}

	private List<CostUnitAssignment> filterByAccountingCode(List<CostUnitAssignment> subAssignments, Integer accountingCode) {
		List<CostUnitAssignment> filterdSubAssignments = new ArrayList<>();
		
		for(CostUnitAssignment assignment : subAssignments) {
			if(assignment.getAccountingCodes().isEmpty() || assignment.getAccountingCodes().contains(accountingCode)) {
				filterdSubAssignments.add(assignment);
			}
		}
		return filterdSubAssignments;
	}
	
	private List<AddressData> convertToCostUnitAddressData(List<CostUnitAssignment> filterdSubAssignments) throws Exception{
		List<AddressData> addressDatas = new ArrayList<>();
		
		for(CostUnitAssignment assignment : filterdSubAssignments) {
			//Verknüpfung -> Verweis vom IK der Versichertenkarte zum Kostenträger wird ausgeschlossen
			if(assignment.getTypeAssignmentId().intValue() != CostUnitTypeAssignment.ASSIGNMENT_IK_HEALTH_INSURANCE_CARD_TO_COST_UNIT) {
				CostUnitInstitution institution = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionByInstitutionNumber(assignment.getInstitutionIdAssignment()).orElseThrow();
				CostUnitTypeAssignment assignmnetType = rFactory.getCostUnitTypeAssignmentRepository().findById(assignment.getTypeAssignmentId()).orElseThrow();
				
				AddressData address = new AddressData();
			    address.setIk(institution.getInstitutionNumber());
			    address.setName1(institution.getFirmName());
			    address.setName2(institution.getShortDescription());
			    address.setAddressOne(institution.getAddressfirst());
			    address.setAddressTwo(institution.getAddressSecond());
			    address.setCostUnitType(assignmnetType.getDescription());
			    addressDatas.add(address);
			}
		}
		return addressDatas;
	}
    
}
