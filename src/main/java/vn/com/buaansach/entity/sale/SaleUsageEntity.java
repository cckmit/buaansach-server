package vn.com.buaansach.entity.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_sale_usage")
@Data
public class SaleUsageEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * FK
     * */

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "order_guid")
    private UUID orderGuid;
}
