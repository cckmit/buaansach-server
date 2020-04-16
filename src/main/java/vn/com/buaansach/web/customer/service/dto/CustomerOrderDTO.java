package vn.com.buaansach.web.customer.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerOrderDTO extends AuditDTO {
    private String orderCode;
    private String orderNote;
    private OrderStatus orderStatus;
    private String customerName;
    private String customerPhone;
    private List<CustomerOrderProductDTO> listProducts;
}
