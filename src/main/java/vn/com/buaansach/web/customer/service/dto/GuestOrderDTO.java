package vn.com.buaansach.web.customer.service.dto;

import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.web.common.service.dto.AuditDTO;

import java.util.List;

public class GuestOrderDTO extends AuditDTO {
    private String orderCode;
    private String orderNote;
    private OrderStatus orderStatus;
    private String customerName;
    private String customerPhone;
    private List<GuestOrderProductDTO> listProducts;
}
