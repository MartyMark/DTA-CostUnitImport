package costunitimport.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.model.CostUnitInstitution;

@Service
public class CostUnitService {
	
	@Autowired
	private RepositoryFactory rFactory;
	
	public Optional<CostUnitInstitution> findCostUnitByIk(Integer institutionNumber){
		return this.rFactory.getCostUnitInstitutionRepository().findByInstitutionNumber(institutionNumber);
	}
	
	public Optional<CostUnitInstitution> findDAVByIkAndValidityFromAndCostUnitSeperation(){
		
	}
}
