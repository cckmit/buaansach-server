package vn.com.buaansach.entity.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.StoreSaleType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_sale")
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreSaleEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_sale_type")
    private StoreSaleType storeSaleType;

    @Column(name = "store_sale_activated")
    private boolean storeSaleActivated;

    /**
     * FK
     */

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "sale_guid")
    private UUID saleGuid;
}
