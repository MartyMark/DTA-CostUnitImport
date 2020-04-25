package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.ZipType;

@Repository
public interface ZipTypeRepository extends JpaRepository<ZipType, Integer> {

}
