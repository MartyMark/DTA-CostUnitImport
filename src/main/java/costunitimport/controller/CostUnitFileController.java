package costunitimport.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import costunitimport.dao.CostUnitFileRepository;
import costunitimport.model.CostUnitFile;

@RestController
public class CostUnitFileController {
	
	@Autowired
	private CostUnitFileRepository repository;
	
	@GetMapping(value = "/costUnitfile")
	public ResponseEntity<List<CostUnitFile>> findById(@RequestParam String filename) {
		List<CostUnitFile> files = repository.findByFileName(filename);
		return new ResponseEntity<>(files, HttpStatus.OK);
	}
}
