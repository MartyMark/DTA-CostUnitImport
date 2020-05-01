package costunitimport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CostUnitAssigmentNotFoundException extends RuntimeException {

	public CostUnitAssigmentNotFoundException(Integer id) {
		super(String.format("Verknüpfung mit ID : %s konnte nicht gefunden werden.", id));
	}
}