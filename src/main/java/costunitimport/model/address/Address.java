package costunitimport.model.address;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KASSE_VERKNUEPFUNG")
public class Address {
	
	private @Id @GeneratedValue Integer id;
    
	private AddressType addressType;
	
	private String zipCode;
	private String location;
	private String street;
	private String postBox;
	private LocalDate validityFrom;
	private LocalDate validityUntil;

	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
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
