package vn.com.buaansach.entity.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bas_voucher_inventory")
@Data
public class VoucherInventoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String code;

    private boolean exported;

    public VoucherInventoryEntity() {
    }

    public VoucherInventoryEntity(String code, boolean exported) {
        this.code = code;
        this.exported = exported;
    }
}
