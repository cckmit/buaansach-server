package vn.com.buaansach.exception;

public class LoginAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super("Login name already used!");
    }

}
