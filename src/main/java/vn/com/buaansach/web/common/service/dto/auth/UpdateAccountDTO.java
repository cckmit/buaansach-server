package vn.com.buaansach.web.common.service.dto.auth;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateAccountDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String phone;

    private String langKey;
}
