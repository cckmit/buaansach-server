package vn.com.buaansach.entity.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_voucher_usage")
@Data
public class VoucherUsageEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Size(max = 20)
    @Column(name = "voucher_code", length = 20)
    private String voucherCode;

    @Size(max = 50)
    @Column(name = "used_by", length = 50)
    private String usedBy;

    @Column(name = "used_date")
    private Instant usedDate;

    @Column(name = "order_guid")
    private UUID orderGuid;
}
