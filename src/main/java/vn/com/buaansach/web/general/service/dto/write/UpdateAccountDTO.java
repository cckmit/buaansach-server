package vn.com.buaansach.web.general.service.dto.write;

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
    @Email
    @Size(min = 5, max = 100)
    private String userEmail;

    @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(min = 10, max = 20)
    private String userPhone;

    @NotBlank
    @Size(min = 1, max = 100)
    private String fullName;

    private Gender userGender;

    private Instant userBirthday;

    @Size(max = 255)
    private String userAddress;

    @Size(max = 10)
    private String langKey;
}
