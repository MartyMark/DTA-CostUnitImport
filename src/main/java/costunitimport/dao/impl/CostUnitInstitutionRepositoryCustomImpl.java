package costunitimport.dao.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import costunitimport.dao.CostUnitInstitutionRepository;
import costunitimport.model.CostUnitInstitution;

@Repository
public class CostUnitInstitutionRepositoryCustomImpl implements CostUnitInstitutionRepositoryCustom {

	@Autowired
	CostUnitInstitutionRepository costUnitInstitutionRepository;
	
	@Override
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethodId(
			Integer careProviderMethodId) {
		
		List<CostUnitInstitution> institutions = costUnitInstitutionRepository.findByCareProviderMethodId(careProviderMethodId);
		
		Map<Integer, List<CostUnitInstitution>> ikToInstitutions =
				institutions.stream().collect(Collectors.groupingBy(CostUnitInstitution::getInstitutionNumber));
		
		final List<CostUnitInstitution> latestCostUnitInstitutions = new ArrayList<>();
		
		for (Map.Entry<Integer, List<CostUnitInstitution>> entry : ikToInstitutions.entrySet()) {
			entry.getValue().sort(Comparator.comparing(o -> o.getValidityFrom()));
			
			latestCostUnitInstitutions.add(entry.getValue().get(0));
		}
		return latestCostUnitInstitutions;
	}

}
