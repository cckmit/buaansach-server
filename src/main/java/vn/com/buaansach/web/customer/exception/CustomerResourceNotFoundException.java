package vn.com.buaansach.web.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CustomerResourceNotFoundException(String message) {
        super(message);
    }
}
