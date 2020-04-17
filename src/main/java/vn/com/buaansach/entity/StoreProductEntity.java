package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_product")
public class StoreProductEntity extends AbstractAuditingEntity implements Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "product_guid")
    private String productGuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_product_status")
    private StoreProductStatus storeProductStatus = StoreProductStatus.AVAILABLE;
}
