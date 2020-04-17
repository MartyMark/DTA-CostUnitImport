package costunitimport.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import costunitimport.model.CostUnitFile;

public interface CostUnitFileRepository extends JpaRepository<CostUnitFile, Integer> {
	List<CostUnitFile> findByFileName(String name);
}
