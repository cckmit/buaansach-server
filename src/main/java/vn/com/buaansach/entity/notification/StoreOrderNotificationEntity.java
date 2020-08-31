package vn.com.buaansach.entity.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreOrderType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_order_notification")
@Data
public class StoreOrderNotificationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_order_type")
    private StoreOrderType storeOrderType;

    @Column(name = "order_product_group")
    private UUID orderProductGroup;

    @Column(name = "number_of_product")
    private int numberOfProduct;

    /**
     * FK
     */

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "store_notification_guid")
    private UUID storeNotificationGuid;
}
