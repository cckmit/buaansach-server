package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.Instant;
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

    /* User account */
    private String userEmail;
    private String userPhone;

    /* User Profile */
    private String userCode;
    private String fullName;
    private String avatarUrl;
    private Gender userGender;
    private Instant userBirthday;
    private String userAddress;

    public AdminStoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity, UserProfileEntity profileEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeUserActivated = storeUserEntity.isStoreUserActivated();
        this.storeGuid = storeUserEntity.getStoreGuid().toString();
        this.userLogin = storeUserEntity.getUserLogin();

        this.userEmail = userEntity.getUserEmail();
        this.userPhone = userEntity.getUserPhone();

        this.userCode = profileEntity.getUserCode();
        this.fullName = profileEntity.getFullName();
        this.avatarUrl = profileEntity.getAvatarUrl();
        this.userGender = profileEntity.getUserGender();
        this.userBirthday = profileEntity.getUserBirthday();
        this.userAddress = profileEntity.getUserAddress();

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

        this.userEmail = adminUserDTO.getUserEmail();
        this.userPhone = adminUserDTO.getUserPhone();

        this.userCode = adminUserDTO.getUserCode();
        this.fullName = adminUserDTO.getFullName();
        this.avatarUrl = adminUserDTO.getAvatarUrl();
        this.userGender = adminUserDTO.getUserGender();
        this.userBirthday = adminUserDTO.getUserBirthday();
        this.userAddress = adminUserDTO.getUserAddress();

        this.createdBy = storeUserEntity.getCreatedBy();
        this.createdDate = storeUserEntity.getCreatedDate();
        this.lastModifiedBy = storeUserEntity.getLastModifiedBy();
        this.lastModifiedDate = storeUserEntity.getLastModifiedDate();
    }
}
