package costunitimport.model.sags;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DTA_SAGS_ABRECHNUNGSCODE")
public class DTAAccumulativeGroupKeyAccountinCode {
	
	@EmbeddedId
	private SAGSAccountingCodePK id;
	
	public DTAAccumulativeGroupKeyAccountinCode() {}
	
	public DTAAccumulativeGroupKeyAccountinCode(Integer sagsId, Integer accountingId) {
		this.id = new SAGSAccountingCodePK(sagsId, accountingId);
	}
	
	public SAGSAccountingCodePK getId() {
		return id;
	}

	public void setId(SAGSAccountingCodePK id) {
		this.id = id;
	}

}
