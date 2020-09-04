package vn.com.buaansach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND.name());
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(ErrorCode message) {
        super(message.name());
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
