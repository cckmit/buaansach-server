package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;

import java.util.UUID;

@Data
public class PosStoreUserDTO {
    private UUID guid;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
    private UUID storeGuid;
    private String userLogin;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public PosStoreUserDTO() {

    }

    public PosStoreUserDTO(StoreUserEntity storeUserEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeGuid = storeUserEntity.getStoreGuid();
        this.userLogin = storeUserEntity.getUserLogin();
    }

    public PosStoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeGuid = storeUserEntity.getStoreGuid();
        this.userLogin = storeUserEntity.getUserLogin();

        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
    }
}
