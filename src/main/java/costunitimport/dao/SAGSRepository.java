package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CareProviderMethod;
import costunitimport.model.sags.SAGS;

@Repository
public interface SAGSRepository extends JpaRepository<SAGS, Integer> {

	List<SAGS> findByCareProviderMethod(CareProviderMethod careProviderMethod);

}
