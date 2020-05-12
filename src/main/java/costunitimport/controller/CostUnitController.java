package costunitimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.model.CostUnitInstitution;
import costunitimport.service.CostUnitService;

@RestController
public class CostUnitController {
	
	@Autowired
	private CostUnitService service;
	
	public CostUnitService getService() {
		return service;
	}
	
	@GetMapping(value = "/costUnit", produces = "application/json")
	public ResponseEntity<CostUnitInstitution> findCostUnitInstitution(@RequestParam Integer careProviderMethodId, @RequestParam Integer ik, @RequestParam Integer accountingCode) throws CostUnitInstitutionNotFoundException, Exception {
		CostUnitInstitution costUnit = service.findCostUnitInstitution(careProviderMethodId, ik, accountingCode).orElseThrow(() -> new CostUnitInstitutionNotFoundException(ik));
		return new ResponseEntity<>(costUnit, HttpStatus.OK);
	}
}
