package vn.com.buaansach.util;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class HttpErrorUtil {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String message;
    private String errors;


    public HttpErrorUtil(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = error;
    }

    public static HttpErrorUtil accessReject(String message) {
        return new HttpErrorUtil(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), message, HttpStatus.NON_AUTHORITATIVE_INFORMATION.getReasonPhrase());
    }

    public static HttpErrorUtil internalError(String message) {
        return new HttpErrorUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static HttpErrorUtil badRequest(String message) {
        return new HttpErrorUtil(HttpStatus.BAD_REQUEST.value(), message, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    public static HttpErrorUtil unauthorized(String message) {
        return new HttpErrorUtil(HttpStatus.UNAUTHORIZED.value(), message, HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    public static HttpErrorUtil notFound(String message) {
        return new HttpErrorUtil(HttpStatus.NOT_FOUND.value(), message, HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    public static HttpErrorUtil forbidden(String message) {
        return new HttpErrorUtil(HttpStatus.FORBIDDEN.value(), message, HttpStatus.FORBIDDEN.getReasonPhrase());
    }

    public HttpErrorUtil error(HttpStatus status, String message) {
        return new HttpErrorUtil(status.value(), message, status.getReasonPhrase());
    }

    public HttpErrorUtil error(HttpStatus status, String message, String error) {
        return new HttpErrorUtil(status.value(), message, error);
    }
}
