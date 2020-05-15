package vn.com.buaansach.web.customer_care.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.CustomerZaloStatus;

@Data
public class CustomerCareUpdateCustomerDTO {
    private String customerPhone;
    private CustomerZaloStatus customerZaloStatus;
}
