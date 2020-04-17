package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
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

    @Size(max = 16)
    @Column(name = "order_code", length = 16)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus = OrderStatus.DRAFT;

    @Size(max = 255)
    @Column(name = "order_note")
    private String orderNote;

    @Size(max = 100)
    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Size(min = 10, max = 12)
    @Column(name = "customer_phone", length = 12)
    private String customerPhone;

    @Column(name = "seat_guid")
    private Long seatGuid;
}
