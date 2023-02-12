package online.strongnation.business.exception;

public class IllegalCountryException extends IllegalOperationException {
    public IllegalCountryException() {
    }

    public IllegalCountryException(String message) {
        super(message);
    }
}
