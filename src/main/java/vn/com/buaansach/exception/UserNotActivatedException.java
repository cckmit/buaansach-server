package vn.com.buaansach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotActivatedException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
