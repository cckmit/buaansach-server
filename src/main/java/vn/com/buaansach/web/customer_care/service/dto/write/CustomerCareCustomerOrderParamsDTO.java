package vn.com.buaansach.web.customer_care.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.CustomerCareStatus;

import java.time.Instant;

@Data
public class CustomerCareCustomerOrderParamsDTO {
    private Instant startDate;
    private Instant endDate;
    private String customerPhone;
    private CustomerCareStatus customerCareStatus;
    private Integer orderCountMin;
    private Integer orderCountMax;
}
