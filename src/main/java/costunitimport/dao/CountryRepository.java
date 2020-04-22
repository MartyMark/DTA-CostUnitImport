package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import costunitimport.model.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {

}
