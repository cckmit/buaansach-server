package vn.com.buaansach.web.general.service.dto.write;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequestDTO {
    @NotNull
    @Size(min = 1, max = 100)
    private String principal;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    private boolean rememberMe;
}
