package costunitimport.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "KASSE_VERKNUEPFUNG")
public class CostUnitAssignment {
	
	private @Id Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private CostUnitAssignment typeAssignment;
	
	private Integer institutionId;
	private Integer institutionIdAssignment;
	private Integer institutionIdAccounting;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private CostUnitTypeDataSupply typeDataSupply;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private CostUnitTypeMedium typeMedium;
	
	private Integer federalStateClassificationId;
	private Integer districtId;
	private String rateCode;
	private LocalDate validityFrom;
	private LocalDate validityUntil;
	
	@OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private List<DTAAccountingCode> accountingCodes;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public CostUnitAssignment getTypeAssignment() {
		return typeAssignment;
	}
	
	public void setTypeAssignment(CostUnitAssignment typeAssignment) {
		this.typeAssignment = typeAssignment;
	}
	
	public Integer getInstitutionId() {
		return institutionId;
	}
	
	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}
	
	public Integer getInstitutionIdAssignment() {
		return institutionIdAssignment;
	}
	
	public void setInstitutionIdAssignment(Integer institutionIdAssignment) {
		this.institutionIdAssignment = institutionIdAssignment;
	}
	
	public Integer getInstitutionIdAccounting() {
		return institutionIdAccounting;
	}
	
	public void setInstitutionIdAccounting(Integer institutionIdAccounting) {
		this.institutionIdAccounting = institutionIdAccounting;
	}
	
	public CostUnitTypeDataSupply getTypeDataSupply() {
		return typeDataSupply;
	}
	
	public void setTypeDataSupply(CostUnitTypeDataSupply typeDataSupply) {
		this.typeDataSupply = typeDataSupply;
	}
	
	public CostUnitTypeMedium getTypeMedium() {
		return typeMedium;
	}
	
	public void setTypeMedium(CostUnitTypeMedium typeMedium) {
		this.typeMedium = typeMedium;
	}
	
	public Integer getFederalStateClassificationId() {
		return federalStateClassificationId;
	}
	
	public void setFederalStateClassificationId(Integer federalStateClassificationId) {
		this.federalStateClassificationId = federalStateClassificationId;
	}
	
	public Integer getDistrictId() {
		return districtId;
	}
	
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	
	public String getRateCode() {
		return rateCode;
	}
	
	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
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
	
	public List<DTAAccountingCode> getAccountingCodes() {
		return accountingCodes;
	}
	
	public void setAccountingCodes(List<DTAAccountingCode> accountingCodes) {
		this.accountingCodes = accountingCodes;
	}
}
