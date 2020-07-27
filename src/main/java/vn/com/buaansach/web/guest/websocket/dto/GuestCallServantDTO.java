package vn.com.buaansach.web.guest.websocket.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GuestCallServantDTO {
    UUID storeGuid;
    UUID seatGuid;
}
