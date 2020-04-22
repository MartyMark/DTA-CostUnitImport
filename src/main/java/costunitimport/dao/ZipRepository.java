package costunitimport.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import costunitimport.model.Zip;

public interface ZipRepository extends JpaRepository<Zip, Integer> {
	Optional<Zip> findByZipCodeAndLocation(String zipCode, String location);
}