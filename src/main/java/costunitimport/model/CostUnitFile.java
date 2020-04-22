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

@Entity
@Table(name = "KOSTENTRÄGER_DATEI")
public class CostUnitFile {
	@Id 
	@GeneratedValue
	private Integer id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private CostUnitSeparation dtaCostUnitSeparation;
    
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private CareProviderMethod careProviderMethod;

    private LocalDate validityFrom;
    private Integer version;
    private String fileName;
    private LocalDateTime creationTime;
	
	public CostUnitSeparation getDtaCostUnitSeparation() {
		return dtaCostUnitSeparation;
	}
	
	public void setDtaCostUnitSeparation(CostUnitSeparation dtaCostUnitSeparation) {
		this.dtaCostUnitSeparation = dtaCostUnitSeparation;
	}
	
	public LocalDate getValidityFrom() {
		return validityFrom;
	}
	
	public void setValidityFrom(LocalDate validityFrom) {
		this.validityFrom = validityFrom;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public CareProviderMethod getCareProviderMethod() {
		return careProviderMethod;
	}

	public void setCareProviderMethod(CareProviderMethod careProviderMethod) {
		this.careProviderMethod = careProviderMethod;
	}
}
