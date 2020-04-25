package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.DTACostUnitSeparation;

@Repository
public interface CostUnitSeparationRepository extends JpaRepository<DTACostUnitSeparation, Integer> {

}
