package costunitimport.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.address.Zip;

@Repository
public interface ZipRepository extends JpaRepository<Zip, Integer> {
	Optional<Zip> findByZipCodeAndLocation(String zipCode, String location);
}
