package vn.com.buaansach.entity.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.VoucherInventoryType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_voucher_inventory")
@Data
public class VoucherInventoryEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Size(max = 30)
    private String code;

    private int length;

    @Enumerated(EnumType.STRING)
    private VoucherInventoryType type;

    private boolean exported;

    public VoucherInventoryEntity() {
    }

    public VoucherInventoryEntity(String code, int length, VoucherInventoryType type, boolean exported) {
        this.code = code;
        this.length = length;
        this.type = type;
        this.exported = exported;
    }
}
