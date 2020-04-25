package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CostUnitFile;

@Repository
public interface CostUnitFileRepository extends JpaRepository<CostUnitFile, Integer> {
	List<CostUnitFile> findByFileName(String name);
}
