package antifraud.exceptions.persistence;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class notFoundException extends RuntimeException {
    public notFoundException(String errorMessage) {
        super(errorMessage);
    }
}
