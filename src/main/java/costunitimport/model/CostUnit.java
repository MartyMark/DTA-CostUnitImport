package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "KOSTENTRÄGER")
public class CostUnit {
	@Id 
	@GeneratedValue
	private Integer id;
}
