package vn.com.buaansach.web.shared.websocket.dto;

import lombok.Data;

@Data
public class DataSocketDTO {
    private int statusCode;
    private String message;
    private Object payload;

    public DataSocketDTO() {
    }

    public DataSocketDTO(int statusCode, String message, Object payload) {
        this.statusCode = statusCode;
        this.message = message;
        this.payload = payload;
    }

    public DataSocketDTO(String message, Object payload) {
        this.message = message;
        this.payload = payload;
    }
}
