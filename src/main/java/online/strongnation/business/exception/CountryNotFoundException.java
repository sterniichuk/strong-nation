package online.strongnation.business.exception;

public class CountryNotFoundException extends NotFoundException {
    public CountryNotFoundException() {
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
