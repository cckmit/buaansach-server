package vn.com.buaansach.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bas_order")
@Data
@EqualsAndHashCode(callSuper = true)
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

    @Size(max = 255)
    @Column(name = "order_note")
    private String orderNote;

    @Column(name = "order_status_timeline")
    private String orderStatusTimeline;

    @Column(name = "order_checkin_time")
    private Instant orderCheckinTime;

    @Column(name = "order_checkout_time")
    private Instant orderCheckoutTime;

    @Size(max = 255)
    @Column(name = "order_cancel_reason")
    private String orderCancelReason;

    @Column(name = "order_discount")
    private int orderDiscount;

    @Column(name = "order_sale_guid")
    private UUID orderSaleGuid;

    @Column(name = "order_voucher_code")
    private String orderVoucherCode;

    @Size(max = 20)
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "seat_guid")
    private UUID seatGuid;

    @Column(name = "payment_guid")
    private UUID paymentGuid;
}
