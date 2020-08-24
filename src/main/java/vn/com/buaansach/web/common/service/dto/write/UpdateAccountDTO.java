package vn.com.buaansach.web.common.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.util.Constants;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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


    @Enumerated(EnumType.STRING)
    private Gender userGender;

    private Instant userBirthday;

    @Size(max = 255)
    private String userAddress;

    @Size(max = 10)
    private String langKey;
}
