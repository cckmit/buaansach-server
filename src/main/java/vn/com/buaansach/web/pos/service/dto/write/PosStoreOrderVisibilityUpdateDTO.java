package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;

import java.util.List;
import java.util.UUID;

@Data
public class PosStoreOrderVisibilityUpdateDTO {
    private UUID storeGuid;
    private List<UUID> listGuid;
    private boolean hidden;
}
