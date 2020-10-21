package vn.com.buaansach.web.shared.service.dto.write;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String domainType;
}
