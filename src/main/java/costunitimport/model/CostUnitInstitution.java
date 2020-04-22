package costunitimport.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KOSTENTRÄGER_INSTITUTION")
public class CostUnitInstitution {
	
	@Id 
	@GeneratedValue
	private Integer id;
	private Integer institutionNumber;
	private String shortDescription;
	private Integer vknr;
	private Address address;
	private LocalDate validityFrom;
	private LocalDate validityUntil;
	private String firmName;
	private LocalDateTime creationTime;
	
	public Integer getInstitutionNumber() {
		return institutionNumber;
	}
	
	public void setInstitutionNumber(Integer institutionNumber) {
		this.institutionNumber = institutionNumber;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public Integer getVknr() {
		return vknr;
	}
	
	public void setVknr(Integer vknr) {
		this.vknr = vknr;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getFirmName() {
		return this.firmName;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}
	
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
}
