package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AdminCreateOrUpdateStoreUserDTO {
    /*used for admin and manager*/
    private UUID guid;
    private UUID storeGuid;

    @Size(min = 1, max = 50)
    private String userLogin;

    @Size(max = 100)
    private String password;

    @Size(min = 1, max = 50)
    private String firstName;

    @Size(min = 1, max = 50)
    private String lastName;

    private StoreUserRole storeUserRole = StoreUserRole.STORE_WAITER;

    private StoreUserStatus storeUserStatus = StoreUserStatus.WORKING;
}
