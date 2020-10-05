package vn.com.buaansach.entity.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_voucher")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VoucherEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 255)
    @Column(name = "voucher_name")
    private String voucherName;

    @Size(max = 255)
    @Column(name = "voucher_name_eng")
    private String voucherNameEng;

    @Size(max = 2000)
    @Column(name = "voucher_description", length = 2000)
    private String voucherDescription;

    @Size(max = 2000)
    @Column(name = "voucher_description_eng", length = 2000)
    private String voucherDescriptionEng;

    @Size(max = 255)
    @Column(name = "voucher_image_url")
    private String voucherImageUrl;

    @Column(name = "voucher_discount")
    private int voucherDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_discount_type")
    private DiscountType voucherDiscountType;

    @Size(max = 500)
    @Column(name = "voucher_conditions", length = 500)
    private String voucherConditions;

    @Column(name = "voucher_activated")
    private boolean voucherActivated;

    @Column(name = "number_voucher_code")
    private int numberVoucherCode;
}
