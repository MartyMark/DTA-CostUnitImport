package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ZEICHENSATZ")
public class CharSet {
	private @Id @GeneratedValue Integer id;

	private String description;
	
	private String charSetId;
	
	public CharSet(String charSetId, String description) {
		this.charSetId = charSetId;
		this.description = description;
	}
	
	public CharSet() {}
	
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

	public String getCharSetId() {
		return charSetId;
	}

	public void setCharSetId(String charSetId) {
		this.charSetId = charSetId;
	}
}