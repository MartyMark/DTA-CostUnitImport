package costunitimport.model.sags;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import costunitimport.model.CareProviderMethod;

@Entity
@Table(name = "SAGS")
public class DTAAccumulativeGroupKey {

	private @Id Integer sagsId;
	
	private String description;
	private String accumlativeGroupkey;
	private Integer careProviderMethodId;
	
	public DTAAccumulativeGroupKey() {}
	
	public DTAAccumulativeGroupKey(Integer sagsId, String desc, String groupKey, CareProviderMethod careProviderMethod) {
		this.sagsId = sagsId;
		this.description = desc;
		this.accumlativeGroupkey = groupKey;
		this.careProviderMethodId = careProviderMethod.getId();
	}
	
	public String getDescription() {
		return description;
	}
	public String getAccumlativeGroupkey() {
		return accumlativeGroupkey;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setAccumlativeGroupkey(String accumlativeGroupkey) {
		this.accumlativeGroupkey = accumlativeGroupkey;
	}
	public Integer getCareProviderMethodId() {
		return careProviderMethodId;
	}
	public void setCareProviderMethod(Integer careProviderMethodId) {
		this.careProviderMethodId = careProviderMethodId;
	}
	public Integer getSagsId() {
		return sagsId;
	}
	public void setSagsId(Integer sagsId) {
		this.sagsId = sagsId;
	}
}
