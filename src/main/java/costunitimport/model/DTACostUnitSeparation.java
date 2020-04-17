package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DTA_KASSENTRENNUNG")
public class DTACostUnitSeparation {
	public static final int AOK = 1; //AOK
	public static final int SUBSTITUTE_HEALTH_INSURANCE = 2; //Ersatzkassen
	public static final int COMPANY_HEALTH_INSURANCE = 3; //Betriebskrankenkassen
	public static final int GUILD_HEALTH_INSURANCE = 4; //Innungskrankenkassen
	public static final int FEDERAL_MINERS_UNION = 5; //Bundesknappschaft
	public static final int AGRICULTURAL_HEALTH_INSURANCE = 6; //Landwirtschaftliche Krankenkassen
	public static final int MARITIME_HEALTH_INSURANCE = 7; //Seekrankenkassen
	public static final int OTHER = 9;// Sonstige
	
	@Id 
	private Integer id;

	private String description;
	
	
}
