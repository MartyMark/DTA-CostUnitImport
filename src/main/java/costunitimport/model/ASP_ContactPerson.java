package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "ANSPRECHPARTNER")
@JsonRootName(value = "ANSPRECHPARTNER")
@XmlRootElement(name = "ANSPRECHPARTNER")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ASP_ContactPerson {
	
	@JsonIgnore
	private @Id @GeneratedValue Integer id;
	
	@JsonProperty("Telefon")
	@XmlElement(name = "Telefon")
	private String telephone;
	
	@JsonProperty("Fax")
	@XmlElement(name = "Fax")
	private String fax;
	
	@JsonProperty("Name")
	@XmlElement(name = "Name")
	private String name;

	@JsonProperty("Arbeitsgebiet des Ansprechpartners")
	@XmlElement(name = "Arbeitsgebiet des Ansprechpartners")
	private String fieldOfActivity;

	public Integer getId() {
		return id;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getFax() {
		return fax;
	}

	public String getName() {
		return name;
	}

	public String getFieldOfActivity() {
		return fieldOfActivity;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFieldOfActivity(String fieldOfActivity) {
		this.fieldOfActivity = fieldOfActivity;
	}
	
}
