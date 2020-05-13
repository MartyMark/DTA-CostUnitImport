package costunitimport.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "KASSE_VERKNUEPFUNG")
public class CostUnitAssignment {
	
	private @Id @GeneratedValue Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private CostUnitAssignment assignment;
	
	private Integer parentInstitutionId;
	private Integer institutionIdAssignment;
	private Integer institutionIdAccounting;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private CostUnitTypeDataSupply typeDataSupply;
	
	private Integer typeMediumId;
	
	private Integer typeAssignmentId;
	
	private Integer federalStateClassificationId;
	private Integer districtId;
	private String rateCode;
	private LocalDate validityFrom;
	private LocalDate validityUntil;
	
	@ElementCollection(targetClass=Integer.class)
	private List<Integer> accountingCodes;
	
	private Integer careProverMethodId;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getTypeAssignmentId() {
		return typeAssignmentId;
	}
	
	public void setTypeAssignmentId(Integer typeAssignmentId) {
		this.typeAssignmentId = typeAssignmentId;
	}
	
	public Integer getParentInstitutionId() {
		return parentInstitutionId;
	}
	
	public void setParentInstitutionId(Integer institutionId) {
		this.parentInstitutionId = institutionId;
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
	
	public Integer getTypeMediumId() {
		return typeMediumId;
	}
	
	public void setTypeMediumId(Integer typeMediumId) {
		this.typeMediumId = typeMediumId;
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
	
	public List<Integer> getAccountingCodes() {
		return accountingCodes;
	}
	
	public void setAccountingCodes(List<Integer> accountingCodes) {
		this.accountingCodes = accountingCodes;
	}

	public CostUnitAssignment getAssignment() {
		return assignment;
	}

	public void setAssignment(CostUnitAssignment assignment) {
		this.assignment = assignment;
	}
	
	public Integer getCareProverMethodId() {
		return careProverMethodId;
	}

	public void setCareProverMethodId(Integer careProverMethodId) {
		this.careProverMethodId = careProverMethodId;
	}
}
