package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PosStoreNotificationVisibilityUpdateDTO {
    private UUID storeGuid;
    private List<UUID> listGuid;
    private boolean hidden;
}
