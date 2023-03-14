package online.strongnation.business.exception.handling;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
public
class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Date timestamp;
    private String message;
    private String debugMessage;

    private ApiError() {
        timestamp = new Date();
    }

    public ApiError(HttpStatus status, String message, String debugMessage) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this(status, message, ex.getLocalizedMessage());
    }
}