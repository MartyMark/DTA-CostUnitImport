package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import costunitimport.model.CareProviderMethod;

public interface CareProviderMethodRepository  extends JpaRepository<CareProviderMethod, Integer> {

}
