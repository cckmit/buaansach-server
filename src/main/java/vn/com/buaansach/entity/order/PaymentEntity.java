package vn.com.buaansach.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.PaymentMethod;
import vn.com.buaansach.entity.enumeration.PaymentStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_payment")
@Data
public class PaymentEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Size(max = 255)
    @Column(name = "payment_note")
    private String paymentNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_discount")
    private int paymentDiscount;

    @Column(name = "payment_discount_type")
    private DiscountType paymentDiscountType;

    @Size(max = 100)
    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Size(max = 50)
    @Column(name = "credit_number", length = 50)
    private String creditNumber;

    @Column(name = "total_amount")
    private long totalAmount;

    @Column(name = "order_guid")
    private UUID orderGuid;
}
