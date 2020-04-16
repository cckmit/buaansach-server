package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class AdminPasswordChangeDTO {
    @Size(max = 50)
    private String login;

    @Size(min = 4, max = 100)
    private String newPassword;
}
