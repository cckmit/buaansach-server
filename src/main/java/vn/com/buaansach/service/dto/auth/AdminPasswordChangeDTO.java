package vn.com.buaansach.service.dto.auth;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class AdminPasswordChangeDTO {
    private String login;

    @Size(min = 4, max = 100)
    private String newPassword;
}
