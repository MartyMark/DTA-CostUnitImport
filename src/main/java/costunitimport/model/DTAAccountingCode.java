package costunitimport.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DTA_ABRECHNUNGSCODE")
public class DTAAccountingCode {
	public static final int CARE_PROVIDER_PHARMACY = 8;//LEISTUNGSERBRINGER FÜR ARZNEIMITTEL UND APOTHEKENÜBLICHE WAREN
	public static final int MIDWIFES = 50;//HEBAMMEN
	
    @Id 
	private String accountingCode;
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "sagsId"), @JoinColumn(name = "accountingId") })
	private List<SAGSDTAAccountingCode> sagsDTAAccountingCode;
    
	public List<SAGSDTAAccountingCode> getSagsDTAAccountingCode() {
		return sagsDTAAccountingCode;
	}

	public void setSagsDTAAccountingCode(List<SAGSDTAAccountingCode> sagsDTAAccountingCode) {
		this.sagsDTAAccountingCode = sagsDTAAccountingCode;
	}

	public String getAccountingCode() {
		return accountingCode;
	}
	
	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Das sind die Ids der Abrechnungscodesgruppenschlüssel bspw : <br>
	 * 00 Sammelschlüssel für alle Leistungsarten , 10 Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)...
	 */
	public static int[] getGroupAccountingCodes() {
		return new int[] {0, 99, 10, 20, 30, 40};
	}
}
