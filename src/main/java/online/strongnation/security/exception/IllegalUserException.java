package online.strongnation.security.exception;

import online.strongnation.business.exception.IllegalOperationException;

public class IllegalUserException extends IllegalOperationException {
    public IllegalUserException(String message) {
        super(message);
    }
}
