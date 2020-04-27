package costunitimport.segment;

public class KTO extends Segment {

	private Integer accountNumber;
	private Integer bankCode;
	private String bankName;
	private String accountHolder;
	private String iban;
	private String bic;

	public KTO(String[] data) {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		accountNumber = getData(position++, Integer.class);
		bankCode = getData(position++, Integer.class);
		bankName = getData(position++, String.class);
		accountHolder = getData(position++, String.class);
		iban = getData(position++, String.class);
		bic = getData(position, String.class);
	}

	/**
	 * Kontonummer
	 * 
	 * @return Kontonummer
	 */
	public Integer getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Bankleitzahl
	 * 
	 * @return Bankleitzahl
	 */
	public Integer getBankCode() {
		return bankCode;
	}

	/**
	 * Bezeichnung der Bank
	 * 
	 * @return Bezeichnung der Bank
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * Kontoinhaber
	 * 
	 * @return Kontoinhaber
	 */
	public String getAccountHolder() {
		return accountHolder;
	}

	/**
	 * IBAN - International Bank Account Number
	 * 
	 * @return IBAN - International Bank Account Number
	 */
	public String getIban() {
		return iban;
	}

	/**
	 * BIC - Business Identifier Code
	 * 
	 * @return BIC - Business Identifier Code
	 */
	public String getBic() {
		return bic;
	}
}
