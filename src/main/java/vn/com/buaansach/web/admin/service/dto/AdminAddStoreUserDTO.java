package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import java.util.UUID;

@Data
public class AdminAddStoreUserDTO {
    private UUID storeGuid;
    private String userLoginOrEmail;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
}
