package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;

import java.util.UUID;

@Data
public class PosStoreProductStatusChangeDTO {
    private UUID storeGuid;
    private UUID storeProductGuid;
    private StoreProductStatus storeProductStatus;
}
