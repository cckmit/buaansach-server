package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AdminCreateOrUpdateStoreUserDTO {
    /*used for admin and manager*/
    private UUID guid;

    private UUID storeGuid;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String userLogin;

    @Email
    @Size(max = 255)
    private String userEmail;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String userPhone;

    @Size(max = 100)
    private String userPassword;

    @Size(min = 1, max = 100)
    private String fullName;

    private StoreUserRole storeUserRole = StoreUserRole.STORE_WAITER;

    private StoreUserStatus storeUserStatus = StoreUserStatus.WORKING;

    public AdminCreateOrUpdateStoreUserDTO() {
    }
}
