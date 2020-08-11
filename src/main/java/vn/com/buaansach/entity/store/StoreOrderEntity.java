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
public class StoreOrderEntity  extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(unique = true)
    private UUID guid;
    @Enumerated(EnumType.STRING)
    private StoreOrderStatus storeOrderStatus;
    @Enumerated(EnumType.STRING)
    private StoreOrderType storeOrderType;
    private String firstSeenBy;
    private String firstHideBy;
    private boolean hidden;
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;
    private UUID orderGuid;
    private UUID orderProductGroup;
    private int numberOfProduct;
}
