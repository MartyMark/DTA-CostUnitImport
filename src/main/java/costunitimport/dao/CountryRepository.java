package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

}
