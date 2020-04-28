package vn.com.buaansach.web.customer.service.dto.write;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerCancelOrderDTO {
    private UUID orderGuid;
    private String cancelReason;
}
