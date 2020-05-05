package costunitimport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServiceApplication extends RuntimeException {
	
	public InternalServiceApplication(String message) {
		super(message);
	}
	
	public InternalServiceApplication(String message, Throwable e) {
		super(message, e);
	}
}
