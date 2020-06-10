package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.time.Instant;
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
    private Gender gender;
    private Instant birthday;
    private String address;
    private String email;
    private String phone;


    public AdminStoreUserDTO() {
    }

    public AdminStoreUserDTO(StoreUserEntity storeUserEntity, UserEntity userEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeGuid = storeUserEntity.getStoreGuid().toString();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.createdBy = storeUserEntity.getCreatedBy();
        this.createdDate = storeUserEntity.getCreatedDate();
        this.lastModifiedBy = storeUserEntity.getLastModifiedBy();
        this.lastModifiedDate = storeUserEntity.getLastModifiedDate();

        this.userCode = userEntity.getCode();
        this.userLogin = userEntity.getLogin();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.activated = userEntity.isActivated();
        this.gender = userEntity.getGender();
        this.birthday = userEntity.getBirthday();
        this.address = userEntity.getAddress();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
    }
}
