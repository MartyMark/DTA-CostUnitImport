package costunitimport.model;

import javax.persistence.Id;

public class CostUnitAssignmentType {
	
	public static final int ASSIGNMENT_IK_HEALTH_INSURANCE_CARD_TO_COST_UNIT =1;
	
	@Id 
	private Integer id;
	
	private String description;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
