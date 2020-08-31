package vn.com.buaansach.web.common.service.dto.write;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserPasswordChangeDTO {
    private String currentPassword;

    @Size(min = 4, max = 100)
    private String newPassword;
}