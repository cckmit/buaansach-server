package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AdminStoreUserDTO extends AuditDTO {
    private UUID guid;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
    private boolean storeUserActivated;
    private String storeGuid;
    private String userLogin;

    /* User Profile */
    private String fullName;

    public AdminStoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity, UserProfileEntity profileEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeUserActivated = storeUserEntity.isStoreUserActivated();
        this.storeGuid = storeUserEntity.getStoreGuid().toString();
        this.userLogin = storeUserEntity.getUserLogin();

        this.fullName = profileEntity.getFullName();

        this.createdBy = storeUserEntity.getCreatedBy();
        this.createdDate = storeUserEntity.getCreatedDate();
        this.lastModifiedBy = storeUserEntity.getLastModifiedBy();
        this.lastModifiedDate = storeUserEntity.getLastModifiedDate();
    }

    public AdminStoreUserDTO(StoreUserEntity storeUserEntity, AdminUserDTO adminUserDTO) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeUserActivated = storeUserEntity.isStoreUserActivated();
        this.storeGuid = storeUserEntity.getStoreGuid().toString();
        this.userLogin = storeUserEntity.getUserLogin();

        this.fullName = adminUserDTO.getFullName();

        this.createdBy = storeUserEntity.getCreatedBy();
        this.createdDate = storeUserEntity.getCreatedDate();
        this.lastModifiedBy = storeUserEntity.getLastModifiedBy();
        this.lastModifiedDate = storeUserEntity.getLastModifiedDate();
    }
}
