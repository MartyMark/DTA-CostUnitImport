package costunitimport.model.response;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import costunitimport.model.address.Address;

/**
 * AdressDaten
 */
@JsonRootName(value = "AdressDaten")
@XmlRootElement(name = "AdressDaten")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressData implements Serializable {

	@JsonIgnore
	private @Id @GeneratedValue Long id;
	
	@JsonProperty("Kassenart")
	@XmlElement(name = "Kassenart", required = false)
	private String costUnitType;
	
	@JsonProperty("Ik")
	@XmlElement(name = "Ik", required = true)
	private Integer ik;

	@JsonProperty("Name1")
	@XmlElement(name = "Name1", required = true)
	private String name1;

	@JsonProperty("Name2")
	@XmlElement(name = "Name2", required = true)
	private String name2;

	@JsonProperty("Name3")
	@XmlElement(name = "Name3", required = true)
	private String name3;

	@JsonProperty("StrasseHnr")
	@XmlElement(name = "StrasseHnr", required = true)
	private String street;

	@JsonProperty("Plz")
	@XmlElement(name = "Plz", required = true)
	private String zipCode;

	@JsonProperty("Ort")
	@XmlElement(name = "Ort", required = true)
	private String location;

	@JsonProperty("Telefon")
	@XmlElement(name = "Telefon", required = true)
	private String phone;

	@JsonProperty("Email")
	@XmlElement(name = "Email", required = true)
	private String email;
	
	@JsonProperty("Anschrift1")
	@XmlElement(name = "Anschrift1", required = true)
	private Address addressOne;
	
	@JsonProperty("Anschrift2")
	@XmlElement(name = "Anschrift2", required = true)
	private Address addressTwo;
	
	/**
	 * IK-Nummer
	 * minimum: 100000000
	 * maximum: 999999999
	 *
	 * @return ik
	 **/
	@JsonProperty("Ik")
	@NotNull
	@Min(100000000)
	@Max(999999999)
	public Integer getIk() {
		return ik;
	}

	/**
	 * Name
	 *
	 * @return name1
	 **/
	@JsonProperty("Name1")
	@NotNull
	@Size(max = 30)
	public String getName1() {
		return name1;
	}

	/**
	 * ggf. Ansprechpartner und Telefonnummer.
	 *
	 * @return name2
	 **/
	@JsonProperty("Name2")
	@Size(max = 30)
	public String getName2() {
		return name2;
	}

	/**
	 * ggf. Ansprechpartner und Telefonnummer.
	 *
	 * @return name3
	 **/
	@JsonProperty("Name3")
	@Size(max = 30)
	public String getName3() {
		return name3;
	}

	/**
	 * Get strasseHnr
	 *
	 * @return strasseHnr
	 **/
	@JsonProperty("StrasseHnr")
	@NotNull
	@Size(max = 30)
	public String getStreet() {
		return street;
	}

	/**
	 * Get plz
	 *
	 * @return plz
	 **/
	@JsonProperty("Plz")
	@NotNull
	@Size(max = 7)
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Get ort
	 *
	 * @return ort
	 **/
	@JsonProperty("Ort")
	@NotNull
	@Size(max = 25)
	public String getLocation() {
		return location;
	}

	/**
	 * Get telefon
	 *
	 * @return telefon
	 **/
	@JsonProperty("Telefon")
	@NotNull
	public String getPhone() {
		return phone;
	}

	/**
	 * Get email
	 *
	 * @return email
	 **/
	@JsonProperty("Email")
	@Size(max = 70)
	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	public void setIk(Integer ik) {
		this.ik = ik;
	}

	
	public void setName1(String name1) {
		this.name1 = name1;
	}

	
	public void setName2(String name2) {
		this.name2 = name2;
	}

	
	public void setName3(String name3) {
		this.name3 = name3;
	}

	
	public void setStreet(String street) {
		this.street = street;
	}

	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	
	public void setLocation(String location) {
		this.location = location;
	}

	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCostUnitType() {
		return costUnitType;
	}

	public void setCostUnitType(String costUnitType) {
		this.costUnitType = costUnitType;
	}

	public Address getAddressOne() {
		return addressOne;
	}

	public Address getAddressTwo() {
		return addressTwo;
	}

	public void setAddressOne(Address addressOne) {
		this.addressOne = addressOne;
	}

	public void setAddressTwo(Address addressTwo) {
		this.addressTwo = addressTwo;
	}
}

