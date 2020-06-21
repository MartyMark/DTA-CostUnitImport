package costunitimport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.TransferParameter;

@Repository
public interface TransferParameterRepository extends JpaRepository<TransferParameter, Integer> {

}
