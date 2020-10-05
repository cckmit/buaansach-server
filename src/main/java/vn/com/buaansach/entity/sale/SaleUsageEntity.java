package vn.com.buaansach.entity.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_sale_usage")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaleUsageEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * FK
     */
    @Column(name = "sale_guid")
    private UUID saleGuid;

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "order_guid")
    private UUID orderGuid;
}
