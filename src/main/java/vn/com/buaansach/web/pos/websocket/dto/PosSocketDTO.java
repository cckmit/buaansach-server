package vn.com.buaansach.web.pos.websocket.dto;

import lombok.Data;

@Data
public class PosSocketDTO {
    private int statusCode;
    private String message;
    private Object payload;

    public PosSocketDTO() {
    }

    public PosSocketDTO(int statusCode, String message, Object payload) {
        this.statusCode = statusCode;
        this.message = message;
        this.payload = payload;
    }

    public PosSocketDTO(String message, Object payload) {
        this.message = message;
        this.payload = payload;
    }
}
