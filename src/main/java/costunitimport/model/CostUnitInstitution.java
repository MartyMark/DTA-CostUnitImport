package costunitimport.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "KOSTENTRÄGER_INSTITUTION")
public class CostUnitInstitution {
	
	@Id 
	@GeneratedValue
	private Integer id;
	private Integer institutionNumber;
	private String shortDescription;
	private Integer vknr;
	private CostUnitAddress currentODAContactAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
	private CareProviderMethod careProviderMethod;
	
	private LocalDate validityFrom;
	private LocalDate validityUntil;
}
