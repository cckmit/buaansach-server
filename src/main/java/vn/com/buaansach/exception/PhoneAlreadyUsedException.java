package vn.com.buaansach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneAlreadyUsedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PhoneAlreadyUsedException() {
        super("Phone number already in use!");
    }
}
