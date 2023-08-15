package antifraud.exceptions.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class unprocessableEntityException extends RuntimeException{
    public unprocessableEntityException(String message) {
        super(message);
    }
}
