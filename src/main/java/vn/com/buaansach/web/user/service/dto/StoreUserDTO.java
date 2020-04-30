package vn.com.buaansach.web.user.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoreUserDTO extends AuditDTO {
    private UUID storeGuid;
    private String storeCode;
    private String storeName;
    private String storeImageUrl;
    private String storeOwnerName;
    private String storeOwnerPhone;
    private String storeOpenHour;
    private String storeCloseHour;
    private StoreStatus storeStatus;
    private String firstName;
    private String lastName;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;

    public StoreUserDTO() {
    }

    public StoreUserDTO(StoreUserEntity storeUserEntity, StoreEntity storeEntity, UserEntity userEntity) {
        this.storeGuid = storeEntity.getGuid();
        this.storeCode = storeEntity.getStoreCode();
        this.storeName = storeEntity.getStoreName();
        this.storeOwnerName = storeEntity.getStoreOwnerName();
        this.storeOwnerPhone = storeEntity.getStoreOwnerPhone();
        this.storeOpenHour = storeEntity.getStoreOpenHour();
        this.storeCloseHour = storeEntity.getStoreCloseHour();
        this.storeImageUrl = storeEntity.getStoreImageUrl();
        this.storeStatus = storeEntity.getStoreStatus();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
    }

}
