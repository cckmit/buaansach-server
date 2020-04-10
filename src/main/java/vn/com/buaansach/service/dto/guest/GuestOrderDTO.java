package vn.com.buaansach.service.dto.guest;

import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.service.dto.core.AuditDTO;

import java.util.List;

public class GuestOrderDTO extends AuditDTO {
    private String orderCode;
    private String orderNote;
    private OrderStatus orderStatus;
    private String customerName;
    private String customerPhone;
    private List<GuestOrderProductDTO> listProducts;
}
