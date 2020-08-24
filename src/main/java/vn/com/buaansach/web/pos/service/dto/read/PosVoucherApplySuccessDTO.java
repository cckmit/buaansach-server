package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.core.service.dto.AuditDTO;

@EqualsAndHashCode(callSuper = true)
@Data
public class PosVoucherApplySuccessDTO extends AuditDTO {
    private String voucherCode;
    private int voucherDiscount;
    private DiscountType voucherDiscountType;

    public PosVoucherApplySuccessDTO() {
    }

    public PosVoucherApplySuccessDTO(PosVoucherCodeDTO voucherCodeDTO) {
        this.voucherCode = voucherCodeDTO.getVoucherCode();
        this.voucherDiscount = voucherCodeDTO.getVoucherDiscount();
        this.voucherDiscountType = voucherCodeDTO.getVoucherDiscountType();
    }
}
