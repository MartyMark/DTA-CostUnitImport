package costunitimport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.exception.DataNotFoundException;
import costunitimport.exception.IncompleteException;
import costunitimport.model.CostUnitAssignment;
import costunitimport.model.CostUnitInstitution;
import costunitimport.model.CostUnitTypeAssignment;
import costunitimport.model.response.AddressData;

@Service
public class CostUnitService {
	
	@Autowired
	private RepositoryFactory rFactory;
	
	public AddressData findCostUnit(Integer kindOfAssignment, Integer careProviderId, Integer institutionnumber,
			Integer accountingCode) {
		CostUnitInstitution parentInstitution = rFactory.getCostUnitInstitutionRepositoryCustom()
				.findLatestCostUnitInstitutionByInstitutionNumber(institutionnumber)
				.orElseThrow(() -> new CostUnitInstitutionNotFoundException(institutionnumber));

		List<CostUnitAssignment> subAssignments = rFactory.getCostUnitAssignmentRepository()
				.findByParentInstitutionIdAndCareProverMethodId(parentInstitution.getInstitutionNumber(),
						careProviderId);

		List<CostUnitAssignment> filterdSubAssignments = filterByAccountingCode(subAssignments, accountingCode);

		List<Integer> institutionsnumbers = filterdSubAssignments.stream()
				.map(CostUnitAssignment::getInstitutionIdAssignment).collect(Collectors.toList());

		/*
		 * Wenn die IDK-Institutionsnummer im VKG Segment vorhanden ist, findet eine
		 * Selbstreferenzierung statt. Die Jetzt vorhanden VKGs stellen die entgültigen
		 * Datenannahmestellen dar
		 */
		if (institutionsnumbers.contains(parentInstitution.getInstitutionNumber())) {
			CostUnitAssignment finalAssignment = filterdSubAssignments.stream()
					.filter(x -> x.getTypeAssignmentId() == kindOfAssignment).findFirst()
					.orElseThrow(() -> new DataNotFoundException());
			return convertToCostUnitAddressData(finalAssignment);
		}

		/*
		 * Es sollte immer nur eine Verknüpfung gefunden werden. Falls es mehrere gibt
		 * ist die Abfrage nicht explizit genug
		 */
		if (institutionsnumbers.size() > 1) {
			throw new IncompleteException(kindOfAssignment, careProviderId, parentInstitution.getInstitutionNumber(),
					accountingCode);
		}

		CostUnitAssignment ins = filterdSubAssignments.get(0);

		return findCostUnit(kindOfAssignment, careProviderId, ins.getInstitutionIdAssignment(), accountingCode);
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
	
	private AddressData convertToCostUnitAddressData(CostUnitAssignment assignment) {
		CostUnitInstitution institution = rFactory.getCostUnitInstitutionRepositoryCustom().findLatestCostUnitInstitutionByInstitutionNumber(assignment.getInstitutionIdAssignment()).orElseThrow();
		CostUnitTypeAssignment assignmnetType = rFactory.getCostUnitTypeAssignmentRepository().findById(assignment.getTypeAssignmentId()).orElseThrow();

		AddressData address = new AddressData();
		address.setIk(institution.getInstitutionNumber());
		address.setName1(institution.getFirmName());
		address.setName2(institution.getShortDescription());
		address.setAddressList(institution.getAddressList());
		address.setCostUnitType(assignmnetType.getDescription());
		address.setContactPersons(institution.getContactPersons());
		address.setTransmissionmedien(institution.getTransferList());
		return address;
	}
    
}
