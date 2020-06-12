package vn.com.buaansach.web.customer_care.service.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CustomerCareStatisticDTO {
    private Instant startDate;
    private Instant endDate;
    private List<CustomerCareCustomerDTO> listCustomer;
}
