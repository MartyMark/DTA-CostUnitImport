package costunitimport.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import costunitimport.dao.SAGSAccountingCodePK;

@Entity
@Table(name = "SAGS_DTA_ABRECHNUNGSCODE")
public class SAGSDTAAccountingCode {
	
	@EmbeddedId
	private SAGSAccountingCodePK id;

	public SAGSAccountingCodePK getId() {
		return id;
	}

	public void setId(SAGSAccountingCodePK id) {
		this.id = id;
	}
}
