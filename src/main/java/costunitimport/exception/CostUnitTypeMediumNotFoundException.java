package costunitimport.exception;

public class CostUnitTypeMediumNotFoundException extends RuntimeException {
	public CostUnitTypeMediumNotFoundException(Integer kindOfDataMedium) {
		super(String.format("Unbekannte Art des Mediums! Id: %s", kindOfDataMedium));
	}
}
