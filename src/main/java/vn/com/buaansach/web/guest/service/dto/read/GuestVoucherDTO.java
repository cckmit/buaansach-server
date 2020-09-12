package vn.com.buaansach.web.guest.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.voucher.VoucherEntity;

import java.util.UUID;

@Data
public class GuestVoucherDTO {
    private UUID guid;
    private String voucherName;
    private String voucherNameEng;
    private String voucherDescription;
    private String voucherDescriptionEng;
    private String voucherImageUrl;
    private int voucherDiscount;
    private DiscountType voucherDiscountType;
    private boolean voucherActivated;

    public GuestVoucherDTO() {
    }

    public GuestVoucherDTO(VoucherEntity voucherEntity) {
        this.guid = voucherEntity.getGuid();
        this.voucherName = voucherEntity.getVoucherName();
        this.voucherNameEng = voucherEntity.getVoucherNameEng();
        this.voucherDescription = voucherEntity.getVoucherDescription();
        this.voucherDescriptionEng = voucherEntity.getVoucherDescriptionEng();
        this.voucherImageUrl = voucherEntity.getVoucherImageUrl();
        this.voucherDiscount = voucherEntity.getVoucherDiscount();
        this.voucherDiscountType = voucherEntity.getVoucherDiscountType();
        this.voucherActivated = voucherEntity.isVoucherActivated();
    }
}
