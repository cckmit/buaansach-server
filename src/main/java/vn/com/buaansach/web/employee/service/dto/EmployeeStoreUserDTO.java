package vn.com.buaansach.web.employee.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.entity.StoreUserEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.common.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeStoreUserDTO extends AuditDTO {
    private UUID storeGuid;
    private String storeCode;
    private String storeName;
    private StoreStatus storeStatus;
    private String firstName;
    private String lastName;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;

    public EmployeeStoreUserDTO() {
    }

    public EmployeeStoreUserDTO(StoreUserEntity storeUserEntity, StoreEntity storeEntity, UserEntity userEntity) {
        this.storeGuid = storeEntity.getGuid();
        this.storeCode = storeEntity.getStoreCode();
        this.storeName= storeEntity.getStoreName();
        this.storeStatus = storeEntity.getStoreStatus();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
    }

}
