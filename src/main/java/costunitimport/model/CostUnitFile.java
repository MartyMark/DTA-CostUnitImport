package costunitimport.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Table(name = "KOSTENTRÄGER_DATEI")
public class CostUnitFile {
	@Id 
	@GeneratedValue
	private Integer id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private DTACostUnitSeparation dtaCostUnitSeparation;
    
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private CareProviderMethod careProviderMethod;

    private LocalDate validityFrom;
    private Integer version;
    private String fileName;
    private LocalDateTime creationTime;
}
