package costunitimport.exception;

public class CareProviderMethodNotFoundException extends RuntimeException {
	public CareProviderMethodNotFoundException(Integer careProviderMethod) {
		super(String.format("Unbekannte Kassenart: %s", careProviderMethod));
	}
}
