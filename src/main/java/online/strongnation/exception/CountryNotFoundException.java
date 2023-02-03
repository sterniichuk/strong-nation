package online.strongnation.exception;

public class CountryNotFoundException extends NotFoundException {
    public CountryNotFoundException() {
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
