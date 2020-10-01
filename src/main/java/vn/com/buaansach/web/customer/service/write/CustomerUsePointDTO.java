package vn.com.buaansach.web.customer.service.write;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerUsePointDTO {
    private UUID orderGuid;
    private int point;
}
