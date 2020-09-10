package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class PosApplySaleDTO {
    private UUID saleGuid;
    private UUID orderGuid;
}
