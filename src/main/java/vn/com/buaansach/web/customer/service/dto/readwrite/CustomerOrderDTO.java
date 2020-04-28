package vn.com.buaansach.web.customer.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vn.com.buaansach.entity.OrderEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.web.customer.service.dto.CustomerOrderProductDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerOrderDTO {
    private UUID guid;
    private String orderCode;
    private OrderStatus orderStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant orderCheckinTime;

    private String customerName;
    private String customerPhone;
    private UUID seatGuid;

    private List<CustomerOrderProductDTO> listOrderProduct;

    public CustomerOrderDTO() {
    }

    public CustomerOrderDTO(OrderEntity orderEntity) {
        this.guid = orderEntity.getGuid();
        this.orderCode = orderEntity.getOrderCode();
        this.orderStatus = orderEntity.getOrderStatus();
        this.orderCheckinTime = orderEntity.getOrderCheckinTime();
        this.customerName = orderEntity.getCustomerName();
        this.customerPhone = orderEntity.getCustomerPhone();
        this.seatGuid = orderEntity.getSeatGuid();
    }
}
