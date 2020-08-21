package vn.com.buaansach.web.guest.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.OrderType;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class GuestOrderDTO {
    private UUID guid;
    private String orderCode;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private String orderStatusTimeline;
    private String orderCancelReason;
    private int orderDiscount;
    private DiscountType orderDiscountType;
    private int orderTotalAmount;
    private String orderCustomerPhone;

    private String orderReceivedBy;
    private Instant orderReceivedDate;
    private String orderPurchasedBy;
    private Instant orderPurchasedDate;
    private String orderCancelledBy;
    private Instant orderCancelledDate;

    private UUID saleGuid;
    private UUID voucherGuid;
    @JsonIgnore
    private String voucherCode;
    private UUID seatGuid;
    private UUID paymentGuid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<GuestOrderProductDTO> listOrderProduct = new ArrayList<>();

    public GuestOrderDTO(OrderEntity orderEntity) {
        this.guid = orderEntity.getGuid();
        this.orderCode = orderEntity.getOrderCode();
        this.orderStatus = orderEntity.getOrderStatus();
        this.orderType = orderEntity.getOrderType();
        this.orderStatusTimeline = orderEntity.getOrderStatusTimeline();
        this.orderCancelReason = orderEntity.getOrderCancelReason();
        this.orderDiscount = orderEntity.getOrderDiscount();
        this.orderDiscountType = orderEntity.getOrderDiscountType();
        this.orderTotalAmount = orderEntity.getOrderTotalAmount();
        this.orderCustomerPhone = orderEntity.getOrderCustomerPhone();

        this.orderReceivedBy = orderEntity.getOrderReceivedBy();
        this.orderReceivedDate = orderEntity.getOrderReceivedDate();
        this.orderPurchasedBy = orderEntity.getOrderPurchasedBy();
        this.orderPurchasedDate = orderEntity.getOrderPurchasedDate();
        this.orderCancelledBy = orderEntity.getOrderCancelledBy();
        this.orderCancelledDate = orderEntity.getOrderCancelledDate();

        this.saleGuid = orderEntity.getSaleGuid();
        this.voucherGuid = orderEntity.getVoucherGuid();
        this.voucherCode = orderEntity.getVoucherCode();
        this.seatGuid = orderEntity.getSeatGuid();
        this.paymentGuid = orderEntity.getPaymentGuid();
    }
}
