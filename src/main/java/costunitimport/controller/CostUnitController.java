package costunitimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import costunitimport.model.response.AddressData;
import costunitimport.service.CostUnitService;

@RestController
public class CostUnitController {
	
	@Autowired
	private CostUnitService service;
	
	/**
	 * Beschafft alle Datenannhmestellen 
	 */
	@GetMapping(value = "/dav", produces = "application/json")
	public ResponseEntity<AddressData> findDAV(@RequestParam Integer type, @RequestParam Integer careProviderMethodId, @RequestParam Integer ik, @RequestParam Integer accountingCode) {
		AddressData costUnits = service.findDAV(type, careProviderMethodId, ik, accountingCode);
		return new ResponseEntity<>(costUnits, HttpStatus.OK);
	}
}
