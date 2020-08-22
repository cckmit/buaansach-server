package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;
import vn.com.buaansach.entity.enumeration.StoreOrderType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_order")
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreOrderEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_order_status")
    private StoreOrderStatus storeOrderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_order_type")
    private StoreOrderType storeOrderType;

    @Column(name = "first_seen_by")
    private String firstSeenBy;

    @Column(name = "first_hide_by")
    private String firstHideBy;

    @Column(name = "hide_store_order")
    private boolean hideStoreOrder;

    @Column(name = "order_product_group")
    private UUID orderProductGroup;

    @Column(name = "number_of_product")
    private int numberOfProduct;

    /**
     * FK
     */

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "area_guid")
    private UUID areaGuid;

    @Column(name = "seat_guid")
    private UUID seatGuid;
}
