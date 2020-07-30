package vn.com.buaansach.web.guest.service.dto.readwrite;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GuestCheckOrderSeatDTO {
    private UUID seatGuid;
    private List<UUID> listOrderGuid;
    private boolean hasValidOrderGuid;
    private UUID activeOrderGuid;
}
