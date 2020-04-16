package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * for admin only
 */
@Data
public class AdminCreateAccountDTO {
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(max = 50)
    private String login;

    @Email
    @Size(max = 255)
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
}
