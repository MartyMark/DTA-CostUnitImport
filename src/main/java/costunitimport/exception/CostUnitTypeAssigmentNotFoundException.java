package costunitimport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CostUnitTypeAssigmentNotFoundException extends RuntimeException {

	public CostUnitTypeAssigmentNotFoundException(Integer id) {
		super(String.format("Verknüpfungstyp mit ID : %s konnte nicht gefunden werden.", id));
	}
}