package online.strongnation.business.exception.handling;

import online.strongnation.business.exception.IllegalOperationException;
import online.strongnation.business.exception.NotFoundException;
import online.strongnation.security.exception.SecurityException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(SecurityException.class)
    protected ResponseEntity<Object> handleNotFound(SecurityException ex) {
        String error = "Security Exception";
        return buildResponseEntity(new ApiError(FORBIDDEN, error, ex));
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        String error = "Not Found Exception";
        return buildResponseEntity(new ApiError(NOT_FOUND, error, ex));
    }

    @ExceptionHandler(IllegalOperationException.class)
    protected ResponseEntity<Object> handleCountryNotFoundException(IllegalOperationException ex) {
        String error = "Illegal Operation Exception";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }
}
