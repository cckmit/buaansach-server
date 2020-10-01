package vn.com.buaansach.web.shared.service.dto.write;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRegisterDTO {
    @NotBlank
    private String fullName;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    private String userLogin;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String userPhone;

    @Email
    private String userEmail;

    @Size(min = 4, max = 100)
    private String userPassword;
}
