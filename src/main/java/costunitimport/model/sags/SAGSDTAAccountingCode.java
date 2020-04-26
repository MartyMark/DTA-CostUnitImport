package costunitimport.model.sags;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import costunitimport.model.DTAAccountingCode;

@Entity
@Table(name = "SAGS_DTA_ABRECHNUNGSCODE")
public class SAGSDTAAccountingCode {
	
	@EmbeddedId
	private SAGSAccountingCodePK id;
	
	@MapsId("sagsId")
    @ManyToOne
    @JoinColumn(name = "sagsId", updatable = false, nullable = false)
	private SAGS sags;
	
	@MapsId("accountingId")
    @ManyToOne
    @JoinColumn(name = "accountingId", updatable = false, nullable = false)
	private DTAAccountingCode accountingCode;

	public SAGS getSags() {
		return sags;
	}

	public void setSags(SAGS sags) {
		this.sags = sags;
	}

	public SAGSAccountingCodePK getId() {
		return id;
	}

	public void setId(SAGSAccountingCodePK id) {
		this.id = id;
	}

	public DTAAccountingCode getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(DTAAccountingCode accountingCode) {
		this.accountingCode = accountingCode;
	}
}
