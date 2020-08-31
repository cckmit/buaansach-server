package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_product")
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreProductEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_product_status")
    private StoreProductStatus storeProductStatus = StoreProductStatus.AVAILABLE;

    @Column(name = "store_product_hidden")
    private boolean storeProductHidden;

    /**
     * FK
     * */

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "product_guid")
    private UUID productGuid;
}
