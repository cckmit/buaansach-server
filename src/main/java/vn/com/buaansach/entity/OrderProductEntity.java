package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_order_product")
@Data
public class OrderProductEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "product_guid")
    private UUID productGuid;

    @Size(max = 16)
    @Column(name = "order_product_group", length = 16)
    private String orderProductGroup;

    @Column(name = "order_product_quantity")
    private int orderProductQuantity;

    @Column(name = "price_each")
    private int orderProductPrice;

    @Size(max = 255)
    @Column(name = "order_product_note")
    private String orderProductNote;



    @Enumerated(EnumType.STRING)
    @Column(name = "order_product_status")
    private OrderProductStatus orderProductStatus;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "order_product_status_timeline")
    private String orderProductStatusTimeline;
}
