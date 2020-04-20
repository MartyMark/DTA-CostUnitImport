package costunitimport.controller;

import org.springframework.beans.factory.annotation.Autowired;

import costunitimport.assembler.AssemblerTools;
import costunitimport.service.CostUnitService;

public class CostUnitController {
	@Autowired
	private CostUnitService service;
	
	@Autowired
	private AssemblerTools tools;
	
}
