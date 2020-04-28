package vn.com.buaansach.web.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CustomerBadRequestException() {
        super("Bad request!");
    }

    public CustomerBadRequestException(String message) {
        super(message);
    }

    public CustomerBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
