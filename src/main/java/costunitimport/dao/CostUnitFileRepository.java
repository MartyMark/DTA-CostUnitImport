package costunitimport.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CostUnitFile;

@Repository
public interface CostUnitFileRepository extends JpaRepository<CostUnitFile, Integer> {
	Optional<CostUnitFile> findByName(String name);
}
