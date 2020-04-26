package costunitimport.model.address;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STAAT_BUNDESLAND")
public class FederalState {
	public static final int UNKNOWN_FEDERAL_STATE_ID = 0;
	//Germany
	public static final int SCHLESWIG_HOLSTEIN = 1;
	public static final int HAMBURG = 2;
	public static final int LOWER_SAXONY = 3;
	public static final int BREMEN = 4;
	public static final int NORTH_RHINE_WESTPHALIA = 5;
	public static final int HESSE = 6;
	public static final int RHINELAND_PALATINATE = 7;
	public static final int BADEN_WUERTTEMBERG = 8;
	public static final int BAVARIA = 9;
	public static final int SAARLAND = 10;
	public static final int BERLIN = 11;
	public static final int BRANDENBURG = 12;
	public static final int MECKLENBURG_WEST_POMERANIA = 13;
	public static final int SAXONY = 14;
	public static final int SAXONY_ANHALT = 15;
	public static final int THURINGIA = 16;
	
	public static final int RHINELAND = 17;
	public static final int WESTPHALIA_LIPPE = 18;
	
	public static final int ALL_FEDERAL_STATES = 99;
	public static final int ALL_FEDERAL_STATES_AT = 199;
	public static final int ALL_CANTON = 299;
	public static final int ALL_PROVINCES = 399;
	
	public static final int UPPER_AUSTRIA = 104;
	
	@Id
	private Integer id;
	
	private String description;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountingCode")
    private Country country;
    private Integer membershipId;
	
	public FederalState() {}

	public FederalState(Integer id, Country country, String description, Integer membershipId) {
		this.id = id;
		this.country = country;
		this.membershipId = membershipId;
		this.description = description;
	}
    
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Integer getMembershipId() {
		return membershipId;
	}
	
	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
