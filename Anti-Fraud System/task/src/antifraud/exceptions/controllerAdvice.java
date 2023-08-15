package antifraud.exceptions;

import antifraud.exceptions.transaction.failedTransactionException;
import antifraud.exceptions.user.alreadyHasRoleException;
import antifraud.models.responses.transactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class controllerAdvice {

    @ExceptionHandler({failedTransactionException.class, alreadyHasRoleException.class})
    public ResponseEntity<transactionResponse> customHandles(
            failedTransactionException e) {

        transactionResponse body = new transactionResponse(
                e.getMessage(),e.info);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        ResponseStatus responseStatusAnnotation = ex.getClass().getAnnotation(ResponseStatus.class);

        if (responseStatusAnnotation != null) {
            return new ResponseEntity<>(
                    Map.of("message", ex.getMessage()), responseStatusAnnotation.value());
        } else {
            return new ResponseEntity<>(
                    Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getBody());
    }


//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
//            HttpRequestMethodNotSupportedException e) {
//
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Object> handleIllegalArgumentException(
//            IllegalArgumentException e) {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
//    }
}
