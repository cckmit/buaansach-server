package vn.com.buaansach.web.guest.websocket.dto;

import lombok.Data;

@Data
public class GuestSocketDTO {
    private String message;
    private Object payload;
}
