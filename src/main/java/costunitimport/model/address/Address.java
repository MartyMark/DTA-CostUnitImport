package costunitimport.model.address;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "KASSE_VERKNUEPFUNG")
@JsonInclude(Include.NON_NULL)
public class Address {
	
	@JsonIgnore
	private @Id @GeneratedValue Integer id;
    
	@JsonProperty("Typ")
	@XmlElement(name = "Typ", required = true)
	private String addressType;
	
	@JsonProperty("Plz")
	@XmlElement(name = "Plz", required = true)
	private String zipCode;
	
	@JsonProperty("Ort")
	@XmlElement(name = "Ort", required = true)
	private String location;
	
	@JsonProperty("Straße")
	@XmlElement(name = "Straße", required = true)
	private String street;
	
	@JsonProperty("Postanschrift")
	@XmlElement(name = "Postanschrift", required = true)
	private String postBox;
	
	@JsonIgnore
	private LocalDate validityFrom;
	
	@JsonIgnore
	private LocalDate validityUntil;

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getPostBox() {
		return postBox;
	}
	
	public void setPostBox(String postBox) {
		this.postBox = postBox;
	}
	
	public LocalDate getValidityFrom() {
		return validityFrom;
	}
	
	public void setValidityFrom(LocalDate validityFrom) {
		this.validityFrom = validityFrom;
	}
	
	public LocalDate getValidityUntil() {
		return validityUntil;
	}
	
	public void setValidityUntil(LocalDate validityUntil) {
		this.validityUntil = validityUntil;
	}

	public Integer getId() {
		return id;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getLocation() {
		return location;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
