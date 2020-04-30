package vn.com.buaansach.entity.voucher.condition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_voucher_single_customer_condition")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherSingleCustomerConditionEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Column(name = "voucher_guid", unique = true)
    private UUID voucherGuid;

    @Size(max = 20)
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;
}
