package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;

import java.util.UUID;

@Data
public class PosStoreUserDTO {
    private UUID guid;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
    private String storeUserArea;
    private UUID storeGuid;
    private UUID userGuid;

    private String userLogin;
    private String userEmail;
    private String userPhone;

    private String fullName;

    public PosStoreUserDTO() {

    }

    public PosStoreUserDTO(StoreUserEntity storeUserEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeUserArea = storeUserEntity.getStoreUserArea();
        this.storeGuid = storeUserEntity.getStoreGuid();
        this.userGuid = storeUserEntity.getUserGuid();
    }

    public PosStoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity, UserProfileEntity profileEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeUserArea = storeUserEntity.getStoreUserArea();
        this.storeGuid = storeUserEntity.getStoreGuid();
        this.userGuid = storeUserEntity.getUserGuid();

        this.userLogin = userEntity.getUserLogin();
        this.userEmail = userEntity.getUserEmail();
        this.userPhone = userEntity.getUserPhone();

        this.fullName = profileEntity.getFullName();
    }
}
