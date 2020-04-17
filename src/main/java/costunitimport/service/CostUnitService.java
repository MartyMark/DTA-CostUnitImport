package costunitimport.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import costunitimport.dao.CostUnitRepository;
import costunitimport.model.CostUnit;

@Service
public class CostUnitService {
	@Autowired
	private CostUnitRepository costUnitRepository;
	
	public Optional<CostUnit> findCostUnitByid(final Integer id){
		return costUnitRepository.findById(id);
	}
	
}
