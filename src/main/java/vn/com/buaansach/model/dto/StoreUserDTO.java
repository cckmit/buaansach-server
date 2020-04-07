package vn.com.buaansach.model.dto;

import lombok.Data;
import vn.com.buaansach.model.entity.StoreUserEntity;
import vn.com.buaansach.model.entity.UserEntity;
import vn.com.buaansach.model.entity.enumeration.StoreUserRole;
import vn.com.buaansach.model.entity.enumeration.StoreUserStatus;

import java.time.Instant;
import java.util.UUID;

@Data
public class StoreUserDTO {
    private UUID guid;
    private String storeGuid;
    private String userLogin;
    private String firstName;
    private String lastName;
    private boolean activated;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;

    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public StoreUserDTO() {
    }

    public StoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity) {
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
