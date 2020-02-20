package vn.com.buaansach.exception;

public class PhoneAlreadyUsedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PhoneAlreadyUsedException() {
        super("Phone is already in use!");
    }
}
