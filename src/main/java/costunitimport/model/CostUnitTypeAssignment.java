package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KASSE_ART_VERKNUEPFUNG")
public class CostUnitTypeAssignment {
	
	public static final int ASSIGNMENT_IK_HEALTH_INSURANCE_CARD_TO_COST_UNIT =1;
	
	@Id 
	private Integer id;
	
	private String description;
	
	CostUnitTypeAssignment(){}
	
	public CostUnitTypeAssignment(Integer id, String description){
		this.id = id;
		this.description = description;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
