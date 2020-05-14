package costunitimport.model.response;

import java.io.Serializable;
import java.util.List;

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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import costunitimport.model.ASP_ContactPerson;
import costunitimport.model.UEM_Transfer;
import costunitimport.model.address.Address;

/**
 * AdressDaten
 */
@JsonRootName(value = "AdressDaten")
@XmlRootElement(name = "AdressDaten")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
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
	
	@JsonProperty("Anschriften")
	@XmlElement(name = "Anschriften", required = true)
	private List<Address> addressList;
	
	@JsonProperty("Ansprechpartner")
	@XmlElement(name = "Ansprechpartner", required = true)
	private List<ASP_ContactPerson> contactPersons;
	
	@JsonProperty("Übermittlungsdaten")
	@XmlElement(name = "Übermittlungsdaten", required = true)
	private List<UEM_Transfer> transferList;
	
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
	
	public String getCostUnitType() {
		return costUnitType;
	}

	public void setCostUnitType(String costUnitType) {
		this.costUnitType = costUnitType;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public List<ASP_ContactPerson> getContactPersons() {
		return contactPersons;
	}

	public void setContactPersons(List<ASP_ContactPerson> contactPersons) {
		this.contactPersons = contactPersons;
	}

	public List<UEM_Transfer> getTransferList() {
		return transferList;
	}

	public void setTransmissionmedien(List<UEM_Transfer> transferList) {
		this.transferList = transferList;
	}
}

