package vn.com.buaansach.web.common.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreUserEntity;

import java.util.UUID;

@Data
public class StoreUserDTO {
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
    private boolean storeUserActivated;
    private UUID storeGuid;

    /* Store Info */
    private String storeCode;
    private String storeName;
    private String storeImageUrl;
    private StoreStatus storeStatus;
    private String storeOwnerName;
    private String storeOwnerPhone;
    private String storeBusinessHours;

    public StoreUserDTO() {
    }

    public StoreUserDTO(StoreUserEntity storeUserEntity, StoreEntity storeEntity) {
        this.storeGuid = storeEntity.getGuid();
        this.storeCode = storeEntity.getStoreCode();
        this.storeName = storeEntity.getStoreName();
        this.storeOwnerName = storeEntity.getStoreOwnerName();
        this.storeOwnerPhone = storeEntity.getStoreOwnerPhone();
        this.storeBusinessHours = storeEntity.getStoreBusinessHours();
        this.storeImageUrl = storeEntity.getStoreImageUrl();
        this.storeStatus = storeEntity.getStoreStatus();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
    }

}
