package vn.com.buaansach.service.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class StoreOwnerChangeDTO {
    private String storeGuid;

    @Size(min = 1, max = 50)
    private String usernameOrEmail;
}
