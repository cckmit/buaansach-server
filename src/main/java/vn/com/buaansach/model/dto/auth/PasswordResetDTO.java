package vn.com.buaansach.model.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordResetDTO {
    @NotBlank
    private String key;

    @Size(min = 4, max = 100)
    private String newPassword;

}
