package vn.com.buaansach.entity.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_sale")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSaleEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

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
