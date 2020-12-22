package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.UUID;

@Data
public class PosStoreDTO {
    private UUID guid;
    private String storeCode;
    private String storeName;
    private StoreStatus storeStatus;
    private String storeAddress;
    private String storeOwnerPhone;
    private boolean storeSeatProtected;

    public PosStoreDTO() {
    }

    public PosStoreDTO(StoreEntity storeEntity) {
        this.guid = storeEntity.getGuid();
        this.storeCode = storeEntity.getStoreCode();
        this.storeName = storeEntity.getStoreName();
        this.storeStatus = storeEntity.getStoreStatus();
        this.storeAddress = storeEntity.getStoreAddress();
        this.storeOwnerPhone = storeEntity.getStoreOwnerPhone();
        this.storeSeatProtected = storeEntity.isStoreSeatProtected();
    }
}
