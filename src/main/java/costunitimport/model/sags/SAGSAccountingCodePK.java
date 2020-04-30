package costunitimport.model.sags;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SAGSAccountingCodePK implements Serializable{

	@Column(name = "sagsId")
	private Integer sagsId;

	@Column(name = "accountingId")
	private Integer accountingId;
	
	public SAGSAccountingCodePK(Integer sagsId, Integer accountingId) {
		this.sagsId = sagsId;
		this.accountingId = accountingId;
	}
	
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