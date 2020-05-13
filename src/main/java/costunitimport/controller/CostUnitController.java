package costunitimport.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.model.response.AddressData;
import costunitimport.service.CostUnitService;

@RestController
public class CostUnitController {
	
	@Autowired
	private CostUnitService service;
	
	public CostUnitService getService() {
		return service;
	}
	
	/**
	 * Beschafft alle Datenannhmestellen 
	 */
	@GetMapping(value = "/costUnit", produces = "application/json")
	public ResponseEntity<List<AddressData>> findDAV(@RequestParam Integer careProviderMethodId, @RequestParam Integer ik, @RequestParam Integer accountingCode) throws CostUnitInstitutionNotFoundException, Exception {
		List<AddressData> costUnits = service.findDAV(careProviderMethodId, ik, accountingCode);
		return new ResponseEntity<>(costUnits, HttpStatus.OK);
	}
}
