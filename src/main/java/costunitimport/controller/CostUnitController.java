package costunitimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.RequestParam;

import costunitimport.assembler.AssemblerTools;
import costunitimport.exception.CostUnitNotFoundException;
import costunitimport.model.CostUnit;
import costunitimport.service.CostUnitService;

public class CostUnitController {
	@Autowired
	private CostUnitService service;
	
	@Autowired
	private AssemblerTools tools;
	
	public EntityModel<CostUnit> findCostUnitById(@RequestParam Integer id) {
		CostUnit costUnit = service.findCostUnitByid(id).orElseThrow(() -> new CostUnitNotFoundException(id));
		return tools.getCostUnitAssembler().toModel(costUnit);
	}
}
