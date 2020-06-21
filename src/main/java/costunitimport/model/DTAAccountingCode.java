package costunitimport.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DTA_ABRECHNUNGSCODE")
public class DTAAccountingCode {
	public static final int CARE_PROVIDER_PHARMACY = 8;//LEISTUNGSERBRINGER FÜR ARZNEIMITTEL UND APOTHEKENÜBLICHE WAREN
	public static final int MIDWIFES = 50;//HEBAMMEN

	private @Id Integer accountingId;
	private String description;
	
	public DTAAccountingCode() {}
	
	public DTAAccountingCode(Integer accountingId, String description) {
		this.accountingId = accountingId;
		this.description = description;
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
	public static List<Integer> getGroupAccountingCodes() {
		return List.of(0, 99, 10, 20, 30, 40);
	}
	
	/**
	 * 10-Gruppenschlüssel Hilfsmittellieferant (Schlüssel 11-19)
	 */
	public static List<Integer> getHimiCodes(){
		return List.of(10, 11, 12, 13, 14, 15, 16, 17, 19);
	}
	
	/**
	 * 20-Gruppenschlüssel Heilmittelerbringer (Schlüssel 21-29)
	 */
	public static List<Integer> getHeimiCodes(){
		return List.of(20, 21, 22, 23, 24, 25, 26, 27, 29);
	}
	
	/**
	 * 30-Gruppenschlüssel Häusliche Krankenpflege (Schlüssel 31-34)
	 */
	public static List<Integer> getHpfCodes(){
		return List.of(30, 31, 32, 33, 34);
	}
	
	/**
	 * 40-Gruppenschlüssel Krankentransportleistungen (Schlüssel 41-49)
	 */
	public static List<Integer> getTransportCodes(){
		return List.of(40, 41, 42, 43, 44, 45, 46, 47, 48, 49);
	}
}
