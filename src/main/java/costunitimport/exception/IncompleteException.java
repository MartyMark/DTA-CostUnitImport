package costunitimport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncompleteException  extends RuntimeException {
	
	public IncompleteException(Integer kindOfAssignment, Integer careProviderId, Integer parentInstitutionIk, Integer accountingCode) {
		super(String.format("Daten sind unvollständig! Art der Verknüpfung : %s, Leistungsbereich : %s, IK : %s, Abrechnungscode : %s", kindOfAssignment, careProviderId, parentInstitutionIk, accountingCode));
	}
}
