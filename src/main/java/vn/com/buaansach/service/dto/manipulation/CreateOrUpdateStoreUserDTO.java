package vn.com.buaansach.service.dto.manipulation;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class CreateOrUpdateStoreUserDTO {
    /**
     * for create new user and add to store immediately
     */
    private UUID guid;
    private UUID storeGuid;

    @Size(min = 1, max = 50)
    private String userLogin;

    @Size(max = 100)
    private String password;
    private String firstName;
    private String lastName;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
}
