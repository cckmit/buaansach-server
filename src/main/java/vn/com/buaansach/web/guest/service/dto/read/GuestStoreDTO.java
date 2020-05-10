package vn.com.buaansach.web.guest.service.dto.read;

import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.UUID;

public class GuestStoreDTO {
    private UUID guid;
    private String storeCode;
    private String storeName;
    private StoreStatus storeStatus;
    private String storeAddress;
    private String storeOwnerPhone;

    public GuestStoreDTO() {
    }

    public GuestStoreDTO(StoreEntity storeEntity) {
        this.guid = storeEntity.getGuid();
        this.storeCode = storeEntity.getStoreCode();
        this.storeName = storeEntity.getStoreName();
        this.storeStatus = storeEntity.getStoreStatus();
        this.storeAddress = storeEntity.getStoreAddress();
        this.storeOwnerPhone = storeEntity.getStoreOwnerPhone();
    }
}
