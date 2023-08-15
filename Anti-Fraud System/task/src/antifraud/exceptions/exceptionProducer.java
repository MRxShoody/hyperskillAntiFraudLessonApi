package antifraud.exceptions;


import antifraud.exceptions.persistence.notFoundException;

public class exceptionProducer {

    public static notFoundException userNotFoundExceptionException() {
        return new notFoundException("User not found");
    }
}
