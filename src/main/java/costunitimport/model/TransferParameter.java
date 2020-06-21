package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ÜBERMITTLUNGSPARAMETER")
public class TransferParameter {
	private @Id Integer id;

	private String description;
	
	public TransferParameter(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public TransferParameter() {}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
