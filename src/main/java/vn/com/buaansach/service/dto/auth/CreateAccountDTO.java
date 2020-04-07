package vn.com.buaansach.service.dto.auth;

import lombok.Data;
import vn.com.buaansach.service.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * for admin only
 */
@Data
public class CreateAccountDTO {
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
