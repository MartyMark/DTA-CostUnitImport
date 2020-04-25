package costunitimport.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SAGS")
public class SAGS {
	@Id
	private Integer id;
	
	private String description;
	private String accumlativeGroupkey;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private DTACareProviderMethod careProviderMethod;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "sagsId"), @JoinColumn(name = "accountingId") })
	private List<SAGSDTAAccountingCode> sagsDTAAccountingCode;
	
	public Integer getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public String getAccumlativeGroupkey() {
		return accumlativeGroupkey;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setAccumlativeGroupkey(String accumlativeGroupkey) {
		this.accumlativeGroupkey = accumlativeGroupkey;
	}
	public DTACareProviderMethod getCareProviderMethod() {
		return careProviderMethod;
	}
	public void setCareProviderMethod(DTACareProviderMethod careProviderMethod) {
		this.careProviderMethod = careProviderMethod;
	}
	public List<SAGSDTAAccountingCode> getSagsDTAAccountingCode() {
		return sagsDTAAccountingCode;
	}
	public void setSagsDTAAccountingCode(List<SAGSDTAAccountingCode> sagsDTAAccountingCode) {
		this.sagsDTAAccountingCode = sagsDTAAccountingCode;
	}
}
