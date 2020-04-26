package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class PosOrderSeatChangeDTO {
    private UUID storeGuid;
    private UUID currentSeatGuid;
    private UUID newSeatGuid;
    private UUID orderGuid;
    private String changeReason;
}
