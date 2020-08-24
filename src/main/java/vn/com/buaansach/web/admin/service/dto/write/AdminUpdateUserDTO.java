package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * for admin only
 */
@Data
public class AdminUpdateUserDTO {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String userLogin;

    @Email
    @Size(min = 5, max = 255)
    private String userEmail;

    @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(min = 10, max = 20)
    private String userPhone;

    private Set<String> authorities;

    @Size(min = 1, max = 100)
    private String fullName;

    @Size(min = 2, max = 10)
    private String langKey;


    public AdminUpdateUserDTO() {
    }
}
