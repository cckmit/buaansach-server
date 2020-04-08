package vn.com.buaansach.model.dto.manipulation;

import lombok.Data;
import vn.com.buaansach.model.entity.enumeration.StoreUserRole;
import vn.com.buaansach.model.entity.enumeration.StoreUserStatus;

import java.util.UUID;

@Data
public class AddStoreUserDTO {
    private UUID storeGuid;
    private String userLoginOrEmail;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
}
