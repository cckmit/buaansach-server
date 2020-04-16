package vn.com.buaansach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("You are not allowed to access this function");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
