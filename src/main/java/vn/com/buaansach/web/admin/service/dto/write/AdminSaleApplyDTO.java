package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class AdminSaleApplyDTO {
    private UUID saleGuid;
    private UUID orderGuid;
}
