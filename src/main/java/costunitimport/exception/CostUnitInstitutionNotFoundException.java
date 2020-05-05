package costunitimport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CostUnitInstitutionNotFoundException extends RuntimeException {

	public CostUnitInstitutionNotFoundException(Integer id) {
		super(String.format("Kasse mit ID : %s konnte nicht gefunden werden.", id));
	}
}
