package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KASSEN_ART_MEDIUM")
public class CostUnitTypeMedium {
	
	@Id
	private Integer id;
	
	private String description;
	
	public CostUnitTypeMedium() {}
	
	public CostUnitTypeMedium(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
	
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
