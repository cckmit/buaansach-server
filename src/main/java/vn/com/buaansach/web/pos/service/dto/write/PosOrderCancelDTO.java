package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class PosOrderCancelDTO {
    private UUID orderGuid;
    private String cancelReason;
}
