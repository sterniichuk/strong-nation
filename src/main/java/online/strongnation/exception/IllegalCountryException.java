package online.strongnation.exception;

public class IllegalCountryException extends RuntimeException{
    public IllegalCountryException() {
    }

    public IllegalCountryException(String message) {
        super(message);
    }
}
