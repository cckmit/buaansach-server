package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class PosOrderProductStatusChangeDTO {
    private UUID storeGuid;
    private UUID orderProductGuid;
    private String orderProductCancelReason;
}
