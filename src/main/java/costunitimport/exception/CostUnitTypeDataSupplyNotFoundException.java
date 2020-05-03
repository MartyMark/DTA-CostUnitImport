package costunitimport.exception;

public class CostUnitTypeDataSupplyNotFoundException extends RuntimeException {
	
	public CostUnitTypeDataSupplyNotFoundException(Integer id) {
		super(String.format("Unbekannte Art der Datenlieferung! Id: %s", id));
	}
}
