package costunitimport.dao;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SAGSAccountingCodePK {
	@Column(name = "sagsId")
	private Integer sagsId;

	@Column(name = "accountingId")
	private Integer accountingId;
	
	public Integer getSagsId() {
		return sagsId;
	}

	public Integer getAccountingId() {
		return accountingId;
	}

	public void setSagsId(Integer sagsId) {
		this.sagsId = sagsId;
	}

	public void setAccountingId(Integer accountingId) {
		this.accountingId = accountingId;
	}
}