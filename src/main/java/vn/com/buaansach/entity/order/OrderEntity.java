package vn.com.buaansach.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.OrderType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bas_order")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 20)
    @Column(name = "order_code", unique = true, length = 20)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;

    @Column(name = "order_status_timeline", length = 3000)
    private String orderStatusTimeline;

    @Size(max = 255)
    @Column(name = "order_cancel_reason")
    private String orderCancelReason;

    @Column(name = "order_discount")
    private int orderDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_discount_type")
    private DiscountType orderDiscountType;

    @Column(name = "order_point_value")
    private int orderPointValue;

    @Column(name = "order_point_cost")
    private int orderPointCost;

    @Column(name = "order_total_amount")
    private int orderTotalAmount;

    @Size(max = 20)
    @Column(name = "order_customer_phone", length = 20)
    private String orderCustomerPhone;

    @Size(max = 50)
    @Column(name = "order_received_by", length = 50)
    private String orderReceivedBy;

    @Column(name = "order_received_date")
    private Instant orderReceivedDate;

    @Size(max = 50)
    @Column(name = "order_purchased_by", length = 50)
    private String orderPurchasedBy;

    @Column(name = "order_purchased_date")
    private Instant orderPurchasedDate;

    @Size(max = 50)
    @Column(name = "order_cancelled_by", length = 50)
    private String orderCancelledBy;

    @Column(name = "order_cancelled_date")
    private Instant orderCancelledDate;

    /**
     * FK
     * */

    @Column(name = "sale_guid")
    private UUID saleGuid;

    @Column(name = "voucher_guid")
    private UUID voucherGuid;

    @Column(name = "voucher_code", length = 30)
    private String voucherCode;

    @Column(name = "seat_guid")
    private UUID seatGuid;

    @Column(name = "payment_guid")
    private UUID paymentGuid;
}
