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

	private @Id @GeneratedValue Integer id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private DTACostUnitSeparation dtaCostUnitSeparation;
    
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private CareProviderMethod careProviderMethod;

    private LocalDate validityFrom;
    private Integer version;
    private String name;
    private LocalDateTime creationTime;
	
	public DTACostUnitSeparation getDtaCostUnitSeparation() {
		return dtaCostUnitSeparation;
	}
	
	public void setDtaCostUnitSeparation(DTACostUnitSeparation dtaCostUnitSeparation) {
		this.dtaCostUnitSeparation = dtaCostUnitSeparation;
	}
	
	public LocalDate getValidityFro() {
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
		return name;
	}
	
	public void setFileName(String fileName) {
		this.name = fileName;
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
