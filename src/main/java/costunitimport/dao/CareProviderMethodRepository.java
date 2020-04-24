package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import costunitimport.model.DTACareProviderMethod;

public interface CareProviderMethodRepository  extends JpaRepository<DTACareProviderMethod, Integer> {

}
