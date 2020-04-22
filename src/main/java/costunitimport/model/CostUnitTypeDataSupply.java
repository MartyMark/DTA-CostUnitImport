package costunitimport.model;

import javax.persistence.Id;

public class CostUnitTypeDataSupply {
	
	@Id
	private Integer id;
	
	private String description;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}
}
