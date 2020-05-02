package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminStoreUserDTO extends AuditDTO {
    private UUID guid;
    private String storeGuid;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;

    private String userCode;
    private String userLogin;
    private String firstName;
    private String lastName;
    private boolean activated;


    public AdminStoreUserDTO() {
    }

    public AdminStoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeGuid = storeUserEntity.getStoreGuid().toString();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();

        this.userCode = userEntity.getCode();
        this.userLogin = userEntity.getLogin();
        this.firstName = userEntity.getFirstName();
        this.activated = userEntity.isActivated();
        this.lastName = userEntity.getLastName();

        this.createdBy = storeUserEntity.getCreatedBy();
        this.createdDate = storeUserEntity.getCreatedDate();
        this.lastModifiedBy = storeUserEntity.getLastModifiedBy();
        this.lastModifiedDate = storeUserEntity.getLastModifiedDate();
    }
}
