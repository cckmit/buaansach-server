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

    @Size(min = 1, max = 50)
    private String firstName;

    @Size(min = 1, max = 50)
    private String lastName;

    @Size(min = 1, max = 50)
    private String login;

    @Email
    @Size(min = 5, max = 255)
    private String email;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String phone;

    @Size(max = 10)
    private String langKey;

    private Set<String> authorities;

    public AdminUpdateUserDTO() {
    }
}