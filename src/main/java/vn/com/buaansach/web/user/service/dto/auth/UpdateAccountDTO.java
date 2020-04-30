package vn.com.buaansach.web.user.service.dto.auth;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
public class UpdateAccountDTO {
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    private String email;

    @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(max = 20)
    private String phone;

    private Gender gender;

    private Instant birthday;

    private String address;

    private String langKey;
}
