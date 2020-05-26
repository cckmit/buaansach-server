package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.voucher.VoucherEntity;

import java.util.UUID;

@Data
public class PosVoucherDTO {
    private UUID guid;
    private String voucherName;
    private String voucherDescription;
    private String voucherImageUrl;
    private int voucherDiscount;
    private DiscountType voucherDiscountType;
    private boolean voucherEnable;

    public PosVoucherDTO() {
    }

    public PosVoucherDTO(VoucherEntity voucherEntity) {
        this.guid = voucherEntity.getGuid();
        this.voucherName = voucherEntity.getVoucherName();
        this.voucherDescription = voucherEntity.getVoucherDescription();
        this.voucherImageUrl = voucherEntity.getVoucherImageUrl();
        this.voucherDiscount = voucherEntity.getVoucherDiscount();
        this.voucherDiscountType = voucherEntity.getVoucherDiscountType();
        this.voucherEnable = voucherEntity.isVoucherEnable();
    }
}
