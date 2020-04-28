package vn.com.buaansach.web.customer.service.dto.write;

import lombok.Data;
import vn.com.buaansach.web.customer.service.dto.CustomerOrderProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerOrderUpdateDTO {
    private UUID orderGuid;
    private List<CustomerOrderProductDTO> listOrderProduct = new ArrayList<>();
}
