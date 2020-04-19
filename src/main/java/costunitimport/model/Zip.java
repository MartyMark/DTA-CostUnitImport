package costunitimport.model;

import lombok.Data;

@Data
public class Zip {
	private Integer id;
	private String zipCode;
	private Country country;
	private FederalState federalState;
}
