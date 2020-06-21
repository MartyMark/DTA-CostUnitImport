package costunitimport.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import costunitimport.model.CharSet;

@Repository
public interface CharSetRepoistory extends JpaRepository<CharSet, Integer> {
	public Optional<CharSet> findByCharSetId(String charSetId);
}
