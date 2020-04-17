package costunitimport.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import costunitimport.dao.CostUnitFileRepository;
import costunitimport.dao.CostUnitRepository;
import lombok.Data;

@Data
@Component
public class RepositoryFactory {
	@Autowired
	private CostUnitRepository costUnitRepository;
	
	@Autowired
	private CostUnitFileRepository costUnitFileRepository;
}
