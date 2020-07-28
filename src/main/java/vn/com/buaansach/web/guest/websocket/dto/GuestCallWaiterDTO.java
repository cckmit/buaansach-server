package vn.com.buaansach.web.guest.websocket.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GuestCallWaiterDTO {
    UUID storeGuid;
    UUID seatGuid;
}
