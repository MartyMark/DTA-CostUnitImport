package costunitimport.model;

import lombok.Data;

@Data
public class CostUnitAssignmentType {
	
	public static final int ASSIGNMENT_IK_HEALTH_INSURANCE_CARD_TO_COST_UNIT =1;
	
	private Integer id;
	private String description;
}
