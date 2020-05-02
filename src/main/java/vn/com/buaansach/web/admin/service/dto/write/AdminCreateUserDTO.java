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
public class AdminCreateUserDTO {
    @Size(max = 20)
    private String code;

    @Size(min = 1, max = 50)
    private String firstName;

    @Size(min = 1, max = 50)
    private String lastName;

    @Size(min = 1, max = 50)
    private String login;

    @Email
    @Size(min = 5, max = 255)
    private String email;

    @Size(max = 12)
    @Pattern(regexp = Constants.PHONE_REGEX)
    private String phone;

    @Size(min = 4, max = 100)
    private String password;

    private boolean activated = true;

    @Size(max = 10)
    private String langKey;

    private Set<String> authorities;

    public AdminCreateUserDTO() {
    }
}
