package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.UserType;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class AdminCreateUserDTO {
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String userLogin;

    @Email
    @Size(max = 255)
    private String userEmail;

    @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(max = 20)
    private String userPhone;

    @Size(min = 4, max = 100)
    private String userPassword;

    private boolean userActivated;

    private UserType userType;

    private Set<String> authorities;

    /* Profile */
    @Size(min = 1, max = 50)
    private String fullName;

    @Size(min = 1, max = 10)
    private String langKey;

    /* Constructor for create user in store */
    public AdminCreateUserDTO(AdminCreateOrUpdateStoreUserDTO dto) {
        this.userLogin = dto.getUserLogin();
        this.userEmail = dto.getUserEmail();
        this.userPhone = dto.getUserPhone();
        this.userPassword = dto.getUserPassword();
        this.userActivated = true;
        this.userType = UserType.INTERNAL;
        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.INTERNAL_USER);
        this.authorities = authorities;
        this.fullName = dto.getFullName();
        this.langKey = Constants.DEFAULT_LANGUAGE;
    }
}
