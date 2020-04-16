package vn.com.buaansach.web.user.service.dto.auth;

import lombok.Data;

@Data
public class JwtTokenDTO {
    private String accessToken;
    private String tokenType;

    public JwtTokenDTO(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
}
