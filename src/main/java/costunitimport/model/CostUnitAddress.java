package costunitimport.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CostUnitAddress {
	private Integer id;
	private Integer ik;
	private Zip zip;
	private String street;
	private String postBox;
	private LocalDate validityFrom;
	private LocalDate validityUntil;
}
