package vn.com.buaansach.entity.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_voucher_code")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VoucherCodeEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Size(max = 30)
    @Column(name = "voucher_code", length = 30, unique = true)
    private String voucherCode;

    @Column(name = "voucher_code_activated")
    private boolean voucherCodeActivated;

    @Column(name = "voucher_code_usage_count")
    private int voucherCodeUsageCount;

    @Size(max = 20)
    @Column(name = "voucher_code_phone", length = 20)
    private String voucherCodePhone;

    /**
     * FK
     */

    @Column(name = "voucher_guid")
    private UUID voucherGuid;
}
