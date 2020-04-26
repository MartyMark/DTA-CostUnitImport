package costunitimport.model.sags;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import costunitimport.model.CareProviderMethod;

@Entity
@Table(name = "SAGS")
public class SAGS {
	@Id
	private Integer sagsId;
	
	private String description;
	private String accumlativeGroupkey;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private CareProviderMethod careProviderMethod;
	
	@OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sagsId")
	private List<SAGSDTAAccountingCode> sagsDTAAccountingCode;
	
	public String getDescription() {
		return description;
	}
	public String getAccumlativeGroupkey() {
		return accumlativeGroupkey;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setAccumlativeGroupkey(String accumlativeGroupkey) {
		this.accumlativeGroupkey = accumlativeGroupkey;
	}
	public CareProviderMethod getCareProviderMethod() {
		return careProviderMethod;
	}
	public void setCareProviderMethod(CareProviderMethod careProviderMethod) {
		this.careProviderMethod = careProviderMethod;
	}
	public List<SAGSDTAAccountingCode> getSagsDTAAccountingCode() {
		return sagsDTAAccountingCode;
	}
	public void setSagsDTAAccountingCode(List<SAGSDTAAccountingCode> sagsDTAAccountingCode) {
		this.sagsDTAAccountingCode = sagsDTAAccountingCode;
	}
	public Integer getSagsId() {
		return sagsId;
	}
	public void setSagsId(Integer sagsId) {
		this.sagsId = sagsId;
	}
}
