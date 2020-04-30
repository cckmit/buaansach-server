package vn.com.buaansach.web.guest.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class GuestOrderDTO {
    private UUID guid;
    private String orderCode;
    private OrderStatus orderStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant orderCheckinTime;

    private String customerName;
    private String customerPhone;
    private UUID seatGuid;

    private List<GuestOrderProductDTO> listOrderProduct;

    public GuestOrderDTO() {
    }

    public GuestOrderDTO(OrderEntity orderEntity) {
        this.guid = orderEntity.getGuid();
        this.orderCode = orderEntity.getOrderCode();
        this.orderStatus = orderEntity.getOrderStatus();
        this.orderCheckinTime = orderEntity.getOrderCheckinTime();
        this.customerName = orderEntity.getCustomerName();
        this.customerPhone = orderEntity.getCustomerPhone();
        this.seatGuid = orderEntity.getSeatGuid();
    }
}
