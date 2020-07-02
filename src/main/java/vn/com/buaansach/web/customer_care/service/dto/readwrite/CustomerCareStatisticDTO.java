package vn.com.buaansach.web.customer_care.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareCustomerDTO;

import java.time.Instant;
import java.util.List;

@Data
public class CustomerCareStatisticDTO {
    private Instant startDate;
    private Instant endDate;
    private List<CustomerCareCustomerDTO> listCustomer;
}
