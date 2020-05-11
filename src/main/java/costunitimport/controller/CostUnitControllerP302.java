package costunitimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import costunitimport.assembler.AssemblerTools;
import costunitimport.exception.CostUnitInstitutionNotFoundException;
import costunitimport.model.CostUnitInstitution;
import costunitimport.service.CostUnitServiceP302;

public class CostUnitControllerP302 {
	
	@Autowired
	private CostUnitServiceP302 service;
	
	@Autowired
	private AssemblerTools tools;
	
	public CostUnitServiceP302 getService() {
		return service;
	}
	
	public AssemblerTools getTools() {
		return tools;
	}
	
	@GetMapping(value = "/costUnit", produces = "application/json")
	public ResponseEntity<CostUnitInstitution> findDAV(@RequestParam Integer ik, @RequestParam Integer separationId, @RequestParam  Integer typeAssignment, @RequestParam Integer typeMediumId, @RequestParam Integer accountingCode) throws CostUnitInstitutionNotFoundException, Exception {
		CostUnitInstitution costUnit = service.findDAV(ik, separationId, typeAssignment, typeMediumId, accountingCode).orElseThrow(() -> new CostUnitInstitutionNotFoundException(ik));
		return new ResponseEntity<>(costUnit, HttpStatus.OK);
	}
	
	@GetMapping(value = "/costUnit", produces = "application/json")
	public ResponseEntity<CostUnitInstitution> findDAV(@RequestParam Integer ik, @RequestParam  Integer typeAssignment, @RequestParam Integer typeMediumId, @RequestParam Integer accountingCode) throws CostUnitInstitutionNotFoundException, Exception {
		CostUnitInstitution costUnit = service.findDAV(ik, typeAssignment, typeMediumId, accountingCode).orElseThrow(() -> new CostUnitInstitutionNotFoundException(ik));
		return new ResponseEntity<>(costUnit, HttpStatus.OK);
	}
}
