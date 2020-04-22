package costunitimport.model;

import javax.persistence.Id;

public class ZipType {
	@Id
	private Integer id;
	
	private String description;
	
	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}
}
