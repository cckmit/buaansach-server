package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class GuestCallWaiterDTO {
    UUID storeGuid;
    UUID seatGuid;
}
