package vn.com.buaansach.entity.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_voucher_code")
@Data
public class VoucherCodeEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Size(max = 20)
    @Column(name = "voucher_code", length = 20, unique = true)
    private String voucherCode;

    @Column(name = "voucher_code_usable")
    private boolean voucherCodeUsable;

    @Column(name = "voucher_code_usage_count")
    private int voucherCodeUsageCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_code_sent_status")
    private VoucherCodeSentStatus voucherCodeSentStatus;

    @Size(max = 20)
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "voucher_guid")
    private UUID voucherGuid;
}
