package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreStatus;

import java.util.UUID;

@Data
public class PosStoreStatusChangeDTO {
    private UUID storeGuid;
    private StoreStatus storeStatus;
}
