package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.StoreUserEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminStoreUserDTO extends AuditDTO {
    private UUID guid;
    private String storeGuid;
    private String userLogin;
    private String firstName;
    private String lastName;
    private boolean activated;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;

    public AdminStoreUserDTO() {
    }

    public AdminStoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeGuid = storeUserEntity.getStoreGuid().toString();
        this.userLogin = userEntity.getLogin();
        this.firstName = userEntity.getFirstName();
        this.activated = userEntity.isActivated();
        this.lastName = userEntity.getLastName();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();

        this.createdBy = storeUserEntity.getCreatedBy();
        this.createdDate = storeUserEntity.getCreatedDate();
        this.lastModifiedBy = storeUserEntity.getLastModifiedBy();
        this.lastModifiedDate = storeUserEntity.getLastModifiedDate();
    }
}
