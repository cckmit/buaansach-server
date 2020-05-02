package vn.com.buaansach.entity.voucher.condition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_voucher_store_condition")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherStoreConditionEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "voucher_guid", unique = true)
    private UUID voucherGuid;

    @Column(name = "store_guid", unique = true)
    private UUID storeGuid;
}
