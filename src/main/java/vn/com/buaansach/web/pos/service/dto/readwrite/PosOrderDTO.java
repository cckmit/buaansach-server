package vn.com.buaansach.web.pos.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class PosOrderDTO extends AuditDTO {
    private UUID guid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderStatusTimeline;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant orderCheckinTime;

    @Size(max = 100)
    private String customerName;

    @Size(max = 12)
    private String customerPhone;

    private UUID seatGuid;

    private UUID recreateFromOrderGuid;

    /* computed */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PosOrderProductDTO> listOrderProduct = new ArrayList<>();

    public PosOrderDTO() {
    }

    public PosOrderDTO(OrderEntity orderEntity) {
        this.guid = orderEntity.getGuid();
        this.orderCode = orderEntity.getOrderCode();
        this.orderStatus = orderEntity.getOrderStatus();
        this.orderStatusTimeline = orderEntity.getOrderStatusTimeline();
        this.orderCheckinTime = orderEntity.getOrderCheckinTime();
        this.customerName = orderEntity.getCustomerName();
        this.customerPhone = orderEntity.getCustomerPhone();
        this.seatGuid = orderEntity.getSeatGuid();
        this.recreateFromOrderGuid = orderEntity.getRecreateFromOrderGuid();

        this.createdBy = orderEntity.getCreatedBy();
        this.createdDate = orderEntity.getCreatedDate();
        this.lastModifiedBy = orderEntity.getLastModifiedBy();
        this.lastModifiedDate = orderEntity.getLastModifiedDate();
    }
}
