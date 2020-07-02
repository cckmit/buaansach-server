package vn.com.buaansach.web.customer_care.service.dto.read;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.OrderType;
import vn.com.buaansach.entity.order.OrderEntity;
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
public class CustomerCareOrderDTO extends AuditDTO {
    private UUID guid;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderCode;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Size(max = 255)
    private String orderNote;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderStatusTimeline;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant orderCheckinTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant orderCheckoutTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderCancelReason;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int orderDiscount;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private DiscountType orderDiscountType;
    private UUID orderSaleGuid;
    @JsonIgnore
    private String orderVoucherCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long totalAmount;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String cashierLogin;
    @Size(max = 20)
    private String customerPhone;
    private UUID seatGuid;
    private UUID paymentGuid;

    /* voucher info */
    private boolean hasVoucher = false;
    private String voucherName;
    private String voucherDescription;
    private String voucherImageUrl;
    private int voucherDiscount;
    private DiscountType voucherDiscountType;
    private Instant voucherCreatedDate;
    private String voucherCustomerPhone;

    /* computed */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CustomerCareOrderProductDTO> listOrderProduct = new ArrayList<>();

    public CustomerCareOrderDTO() {
    }

    public CustomerCareOrderDTO(OrderEntity orderEntity) {
        assignProperty(orderEntity);
    }

    public void updateVoucherAttribute(CustomerCareVoucherCodeDTO dto) {
        if (dto != null) {
            this.hasVoucher = true;
            this.voucherName = dto.getVoucherName();
            this.voucherDescription = dto.getVoucherDescription();
            this.voucherImageUrl = dto.getVoucherImageUrl();
            this.voucherDiscount = dto.getVoucherDiscount();
            this.voucherDiscountType = dto.getVoucherDiscountType();
            this.voucherCreatedDate = dto.getCreatedDate();
            this.voucherCustomerPhone = dto.getCustomerPhone();
        } else {
            this.hasVoucher = false;
        }
    }

    private void assignProperty(OrderEntity orderEntity) {
        this.guid = orderEntity.getGuid();
        this.orderCode = orderEntity.getOrderCode();
        this.orderStatus = orderEntity.getOrderStatus();
        this.orderType = orderEntity.getOrderType();
        this.orderNote = orderEntity.getOrderNote();
        this.orderStatusTimeline = orderEntity.getOrderStatusTimeline();
        this.orderCheckinTime = orderEntity.getOrderCheckinTime();
        this.orderCheckoutTime = orderEntity.getOrderCheckoutTime();
        this.orderCancelReason = orderEntity.getOrderCancelReason();
        this.orderDiscount = orderEntity.getOrderDiscount();
        this.orderDiscountType = orderEntity.getOrderDiscountType();
        this.orderSaleGuid = orderEntity.getOrderSaleGuid();
        this.orderVoucherCode = orderEntity.getOrderVoucherCode();
        this.totalAmount = orderEntity.getTotalAmount();
        this.cashierLogin = orderEntity.getCashierLogin();
        this.customerPhone = orderEntity.getCustomerPhone();
        this.seatGuid = orderEntity.getSeatGuid();
        this.paymentGuid = orderEntity.getPaymentGuid();

        this.createdBy = orderEntity.getCreatedBy();
        this.createdDate = orderEntity.getCreatedDate();
        this.lastModifiedBy = orderEntity.getLastModifiedBy();
        this.lastModifiedDate = orderEntity.getLastModifiedDate();
    }
}