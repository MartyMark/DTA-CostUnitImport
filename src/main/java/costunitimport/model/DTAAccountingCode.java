package costunitimport.model;

import javax.persistence.Id;

/**
 * INF.DTA_ABRECHNUNGSCODE
 */
public class DTAAccountingCode {
	public static final int CARE_PROVIDER_PHARMACY = 8;//LEISTUNGSERBRINGER FÜR ARZNEIMITTEL UND APOTHEKENÜBLICHE WAREN
	public static final int MIDWIFES = 50;//HEBAMMEN
	
    @Id 
	private Integer id;
    
	private String accountingCode;
	private String description;
    
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
