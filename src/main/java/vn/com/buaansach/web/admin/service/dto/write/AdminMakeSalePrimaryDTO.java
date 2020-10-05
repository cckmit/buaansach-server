package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class AdminMakeSalePrimaryDTO {
    private UUID storeGuid;
    private UUID saleGuid;
    boolean revert;
}
