package vn.com.buaansach.service.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordResetDTO {
    @NotBlank
    private String key;

    @Size(min = 4, max = 100)
    private String newPassword;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordDTO{" +
                "key='" + key + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
