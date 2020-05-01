package costunitimport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CostUnitSeperationNotFoundException extends RuntimeException {
	
	public CostUnitSeperationNotFoundException(Integer costUnitSeperationId) {
		super(String.format("Unbekannte Kassenart: %s", costUnitSeperationId));
	}
}
