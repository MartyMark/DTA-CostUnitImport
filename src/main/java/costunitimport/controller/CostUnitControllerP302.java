package costunitimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import costunitimport.assembler.AssemblerTools;
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
	
}
