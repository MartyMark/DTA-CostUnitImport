package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.sags.DTAAccumulativeGroupKey;

@Repository
public interface DTAAccumulativeGroupKeyRepository extends JpaRepository<DTAAccumulativeGroupKey, Integer> {

	List<DTAAccumulativeGroupKey> findByCareProviderMethod(CareProviderMethod careProviderMethod);

}
