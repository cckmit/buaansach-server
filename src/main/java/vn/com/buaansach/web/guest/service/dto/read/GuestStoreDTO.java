package vn.com.buaansach.web.guest.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.UUID;

@Data
public class GuestStoreDTO {
    private UUID guid;
    private String storeCode;
    private String storeName;
    private StoreStatus storeStatus;
    private String storeAddress;
    private String storeOwnerName;
    private String storeOwnerPhone;
    private String storeOwnerEmail;
    private String storeTaxCode;
    private String storeOpenHour;
    private String storeCloseHour;

    public GuestStoreDTO() {
    }

    public GuestStoreDTO(StoreEntity storeEntity) {
        this.guid = storeEntity.getGuid();
        this.storeCode = storeEntity.getStoreCode();
        this.storeName = storeEntity.getStoreName();
        this.storeStatus = storeEntity.getStoreStatus();
        this.storeAddress = storeEntity.getStoreAddress();
        this.storeOwnerName = storeEntity.getStoreOwnerName();
        this.storeOwnerPhone = storeEntity.getStoreOwnerPhone();
        this.storeOwnerEmail = storeEntity.getStoreOwnerEmail();
        this.storeTaxCode = storeEntity.getStoreTaxCode();
        this.storeOpenHour = storeEntity.getStoreOpenHour();
        this.storeCloseHour = storeEntity.getStoreCloseHour();
    }
}
