package vn.com.buaansach.web.general.service.dto.read;

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
