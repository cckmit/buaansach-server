package vn.com.buaansach.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_order_product")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrderProductEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Column(name = "order_product_group")
    private UUID orderProductGroup;

    @Column(name = "order_product_quantity")
    private int orderProductQuantity;

    @Size(max = 255)
    @Column(name = "order_product_note")
    private String orderProductNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_product_status")
    private OrderProductStatus orderProductStatus;

    @Size(max = 1000)
    @Column(name = "order_product_status_timeline")
    private String orderProductStatusTimeline;

    @Size(max = 255)
    @Column(name = "order_product_cancel_reason")
    private String orderProductCancelReason;

    @Column(name = "order_product_root_price")
    private int orderProductRootPrice;

    @Column(name = "order_product_price")
    private int orderProductPrice;

    @Column(name = "order_product_discount")
    private int orderProductDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_product_discount_type")
    private DiscountType orderProductDiscountType;

    /**
     * FK
     */

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "product_guid")
    private UUID productGuid;

    @Column(name = "sale_guid")
    private UUID saleGuid;
}
