package vn.com.buaansach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super("Bad request!");
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String action, String field, String status) {
        super(String.format("error.badRequest;%s;%s;%s", action, field, status));
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
