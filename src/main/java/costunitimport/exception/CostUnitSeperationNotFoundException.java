package costunitimport.exception;

public class CostUnitSeperationNotFoundException extends RuntimeException {
	
	public CostUnitSeperationNotFoundException(Integer costUnitSeperationId) {
		super(String.format("Unbekannte Kassenart: %s", costUnitSeperationId));
	}
}
