package costunitimport.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Klasse zum laden der einzelnen ResourceAssembler
 * Diese fügen Links(Resourcen) an die ResponseBody hinzu
 */
@Component
public class AssemblerTools {
	
	@Autowired
	private CostUnitInstitutionResourceAssembler costUnitInstitutionAssembler;
	
	public CostUnitInstitutionResourceAssembler getCostUnitInstitutionAssembler() {
		return costUnitInstitutionAssembler;
	}
}
