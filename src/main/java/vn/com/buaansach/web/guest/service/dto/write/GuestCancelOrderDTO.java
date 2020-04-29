package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class GuestCancelOrderDTO {
    private UUID orderGuid;
    private String cancelReason;
}
