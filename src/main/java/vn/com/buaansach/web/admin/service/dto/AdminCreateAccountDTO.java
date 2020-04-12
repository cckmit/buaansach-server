package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * for admin only
 */
@Data
public class AdminCreateAccountDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String login;

    @Email
    private String email;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String phone;

    private String password;

    private boolean activated;

    private String langKey;

    private Set<String> authorities;
}
