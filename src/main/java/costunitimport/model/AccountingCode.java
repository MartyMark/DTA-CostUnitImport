package costunitimport.model;

import lombok.Data;

@Data
public class AccountingCode {
	public static final int CARE_PROVIDER_PHARMACY = 8;//LEISTUNGSERBRINGER FÜR ARZNEIMITTEL UND APOTHEKENÜBLICHE WAREN
	public static final int MIDWIFES = 50;//HEBAMMEN
	
	private String accountingCode;
	private Integer id;
    private String description;
}
