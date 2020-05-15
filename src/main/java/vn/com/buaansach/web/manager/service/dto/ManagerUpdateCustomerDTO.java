package vn.com.buaansach.web.manager.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.CustomerZaloStatus;

@Data
public class ManagerUpdateCustomerDTO {
    private String customerPhone;
    private CustomerZaloStatus customerZaloStatus;
}
