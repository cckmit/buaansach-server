package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class GuestCreateOrderDTO {
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;
    private String customerPhone;
}
