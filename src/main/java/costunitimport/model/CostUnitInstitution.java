package costunitimport.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import costunitimport.model.address.Address;

@Entity
@Table(name = "KOSTENTRÄGER_INSTITUTION")
public class CostUnitInstitution {
	
	@Id 
	@GeneratedValue
	private Integer id;
	private Integer institutionNumber;
	private String shortDescription;
	private Integer vknr;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private Address address;
	
	private LocalDate validityFrom;
	private LocalDate validityUntil;
	private String firmName;
	private LocalDateTime creationTime;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private CareProviderMethod careProviderMethod;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private DTACostUnitSeparation costUnitSeparation;
	
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

	public DTACostUnitSeparation getCostUnitSeparation() {
		return costUnitSeparation;
	}

	public void setCostUnitSeparation(DTACostUnitSeparation costUnitSeparation) {
		this.costUnitSeparation = costUnitSeparation;
	}

	public void setCareProviderMethod(CareProviderMethod careProviderMethod) {
		this.careProviderMethod = careProviderMethod;
	}
	
	public CareProviderMethod getCareProviderMethod() {
		return careProviderMethod;
	}
}
