package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PosOrderProductServeDTO {
    private UUID storeGuid;
    private UUID orderGuid;
    private List<UUID> listOrderProductGuid;
}
