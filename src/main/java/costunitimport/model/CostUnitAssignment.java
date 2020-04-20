package costunitimport.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CostUnitAssignment {
	private Integer id;
	private CostUnitAssignment typeAssignment;
	private Integer institutionId;
	private Integer institutionIdAssignment;
	private Integer institutionIdAccounting;
	private CostUnitDataSupplyType typeDataSupply;
	private CostUnitMediumType typeMedium;
	private Integer federalStateClassificationId;
	private Integer districtId;
	private String rateCode;
	private LocalDate validityFrom;
	private LocalDate validityUntil;
	private List<AccountingCode> accountingCodes;
}
