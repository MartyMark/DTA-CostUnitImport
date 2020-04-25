package costunitimport.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.DTACareProviderMethod;
import costunitimport.model.SAGS;

@Repository
public interface SAGSRepository extends JpaRepository<SAGS, Integer> {

	List<SAGS> findByDTACareProviderMethod(DTACareProviderMethod careProviderMethod);

}
