package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AdminAddStoreUserDTO {
    @NotNull
    private UUID storeGuid;
    @NotBlank
    @Size(max = 255)
    private String principal;
    @NotNull
    private StoreUserRole storeUserRole;
    @NotNull
    private StoreUserStatus storeUserStatus;

    private String storeUserArea;
}
