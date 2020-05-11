package costunitimport.dao.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
	public List<CostUnitInstitution> findLatestCostUnitInstitutionsByCareProviderMethodIdAndCostUnitSeparationId(Integer careProviderMethodId, Integer costUnitSeparationId) {
		List<CostUnitInstitution> institutions = costUnitInstitutionRepository.findByCareProviderMethodIdAndCostUnitSeparationId(careProviderMethodId, costUnitSeparationId);
		
		return findLatestCostUnitInstitutions(institutions);
	}

	@Override
	public Map<Integer, CostUnitInstitution> findIKToLatestInstituinMapByCareProviderIdAndCostUnitSeparationId(Integer careProviderMethodId, Integer costUnitSeparation) {
		List<CostUnitInstitution> existingInstitutions = findLatestCostUnitInstitutionsByCareProviderMethodIdAndCostUnitSeparationId(careProviderMethodId, costUnitSeparation);

		// IK - Kasseninstitution
		return existingInstitutions.stream().collect(Collectors.toMap(CostUnitInstitution::getInstitutionNumber, Function.identity()));
	}

	@Override
	public Optional<CostUnitInstitution> findLatestCostUnitInstitutionByInstitutionNumberAndCostUnitSeparationId(Integer institutionNumber, Integer costUnitSeparationId) throws Exception {
		List<CostUnitInstitution> institutions = costUnitInstitutionRepository.findByInstitutionNumberAndCostUnitSeparationId(institutionNumber, costUnitSeparationId);
		
		institutions = findLatestCostUnitInstitutions(institutions);
		
		if(institutions.size() > 1) {
			throw new Exception();
		}
		
		return Optional.ofNullable(institutions.get(0));
	}
	
	@Override
	public Optional<CostUnitInstitution> findLatestCostUnitInstitutionByInstitutionNumber(Integer institutionNumber) throws Exception {
		List<CostUnitInstitution> institutions = costUnitInstitutionRepository.findByInstitutionNumber(institutionNumber);
		
		institutions = findLatestCostUnitInstitutions(institutions);
		
		if(institutions.size() > 1) {
			throw new Exception();
		}
		
		return Optional.ofNullable(institutions.get(0));
	}

	private List<CostUnitInstitution> findLatestCostUnitInstitutions(List<CostUnitInstitution> institutions) {
		Map<Integer, List<CostUnitInstitution>> ikToInstitutions = institutions.stream().collect(Collectors.groupingBy(CostUnitInstitution::getInstitutionNumber));
		
		final List<CostUnitInstitution> latestCostUnitInstitutions = new ArrayList<>();
		
		for (Map.Entry<Integer, List<CostUnitInstitution>> entry : ikToInstitutions.entrySet()) {
			entry.getValue().sort(Comparator.comparing(o -> o.getValidityFrom()));
			
			latestCostUnitInstitutions.add(entry.getValue().get(0));
		}
		return latestCostUnitInstitutions;
	}
}
