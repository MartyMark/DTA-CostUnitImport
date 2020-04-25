package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.DTACareProviderMethod;

@Repository
public interface CareProviderMethodRepository  extends JpaRepository<DTACareProviderMethod, Integer> {

}
