package vn.com.buaansach.web.guest.websocket.dto;

import lombok.Data;

@Data
public class GuestSocketDTO {
    private int statusCode;
    private String message;
    private Object payload;

    public GuestSocketDTO() {
    }

    public GuestSocketDTO(int statusCode, String message, Object payload) {
        this.statusCode = statusCode;
        this.message = message;
        this.payload = payload;
    }

    public GuestSocketDTO(String message, Object payload) {
        this.message = message;
        this.payload = payload;
    }
}
