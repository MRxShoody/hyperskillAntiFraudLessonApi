package antifraud.exceptions.persistence;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class alreadyExistException extends RuntimeException {

    public alreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
