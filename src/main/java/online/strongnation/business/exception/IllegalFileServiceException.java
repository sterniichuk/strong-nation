package online.strongnation.business.exception;

public class IllegalFileServiceException extends IllegalOperationException {
    public IllegalFileServiceException(String message) {
        super(message);
    }

    public IllegalFileServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
