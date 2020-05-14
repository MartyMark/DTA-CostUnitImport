package costunitimport.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import costunitimport.model.address.Address;

@Entity
@Table(name = "KOSTENTRÄGER_INSTITUTION")
public class CostUnitInstitution {
	
	private @Id @GeneratedValue Integer id;
	private Integer institutionNumber;
	private String shortDescription;
	private Integer vknr;
	
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id", nullable=false)
	private Address addressfirst;
	
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id", nullable=true)
	private Address addressSecond;
	
	private LocalDate validityFrom;
	private LocalDate validityUntil;
	private String firmName;
	private LocalDateTime creationTime;
	private Integer careProviderMethodId;
	private Integer costUnitSeparationId;
	
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

	public Integer getCostUnitSeparationId() {
		return costUnitSeparationId;
	}

	public void setCostUnitSeparation(Integer costUnitSeparationId) {
		this.costUnitSeparationId = costUnitSeparationId;
	}

	public void setCareProviderMethodId(Integer careProviderMethodId) {
		this.careProviderMethodId = careProviderMethodId;
	}
	
	public Integer getCareProviderMethod() {
		return careProviderMethodId;
	}

	public Address getAddressfirst() {
		return addressfirst;
	}

	public Address getAddressSecond() {
		return addressSecond;
	}

	public void setAddressfirst(Address addressfirst) {
		this.addressfirst = addressfirst;
	}

	public void setAddressSecond(Address addressSecond) {
		this.addressSecond = addressSecond;
	}
}
