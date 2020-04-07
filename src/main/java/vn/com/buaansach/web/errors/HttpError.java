package vn.com.buaansach.web.errors;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class HttpError {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String message;
    private String errors;


    public HttpError(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = error;
    }

    public static HttpError accessReject(String message) {
        return new HttpError(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), message, HttpStatus.NON_AUTHORITATIVE_INFORMATION.getReasonPhrase());
    }

    public static HttpError internalError(String message) {
        return new HttpError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static HttpError badRequest(String message) {
        return new HttpError(HttpStatus.BAD_REQUEST.value(), message, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    public static HttpError unauthorized(String message) {
        return new HttpError(HttpStatus.UNAUTHORIZED.value(), message, HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    public static HttpError notFound(String message) {
        return new HttpError(HttpStatus.NOT_FOUND.value(), message, HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    public static HttpError forbidden(String message) {
        return new HttpError(HttpStatus.FORBIDDEN.value(), message, HttpStatus.FORBIDDEN.getReasonPhrase());
    }

    public HttpError error(HttpStatus status, String message) {
        return new HttpError(status.value(), message, status.getReasonPhrase());
    }

    public HttpError error(HttpStatus status, String message, String error) {
        return new HttpError(status.value(), message, error);
    }
}
