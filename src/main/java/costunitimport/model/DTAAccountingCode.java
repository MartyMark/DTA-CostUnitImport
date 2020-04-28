package costunitimport.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import costunitimport.model.sags.DTAAccumulativeGroupKeyAccountinCode;

@Entity
@Table(name = "DTA_ABRECHNUNGSCODE")
public class DTAAccountingCode {
	public static final int CARE_PROVIDER_PHARMACY = 8;//LEISTUNGSERBRINGER FÜR ARZNEIMITTEL UND APOTHEKENÜBLICHE WAREN
	public static final int MIDWIFES = 50;//HEBAMMEN
	
    @Id 
	private Integer accountingId;
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountingId")
	private List<DTAAccumulativeGroupKeyAccountinCode> sagsDTAAccountingCode;
    
	public List<DTAAccumulativeGroupKeyAccountinCode> getSagsDTAAccountingCode() {
		return sagsDTAAccountingCode;
	}

	public void setSagsDTAAccountingCode(List<DTAAccumulativeGroupKeyAccountinCode> sagsDTAAccountingCode) {
		this.sagsDTAAccountingCode = sagsDTAAccountingCode;
	}

	public Integer getAccountingCode() {
		return accountingId;
	}
	
	public void setAccountingCode(Integer accountingId) {
		this.accountingId = accountingId;
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
