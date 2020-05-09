package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherStoreConditionEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
public class PosVoucherCodeDTO extends AuditDTO {
    private String voucherCode;

    private boolean voucherCodeUsable;

    private int voucherCodeUsageCount;

    private String customerPhone;

    private String voucherName;
    private String voucherDescription;
    private String voucherImageUrl;
    private int voucherDiscount;
    @Enumerated(EnumType.STRING)
    private DiscountType voucherDiscountType;
    private String voucherConditions;
    private boolean voucherEnable;


    private VoucherTimeConditionEntity timeCondition;
    private VoucherUsageConditionEntity usageCondition;
    private VoucherStoreConditionEntity storeCondition;

    public PosVoucherCodeDTO() {
    }

    public PosVoucherCodeDTO(VoucherCodeEntity voucherCodeEntity, VoucherEntity voucherEntity) {
        assignProperty(voucherCodeEntity, voucherEntity);
    }

    public PosVoucherCodeDTO(VoucherCodeEntity voucherCodeEntity, VoucherEntity voucherEntity, VoucherTimeConditionEntity timeConditionEntity, VoucherUsageConditionEntity usageConditionEntity, VoucherStoreConditionEntity storeConditionEntity) {
        assignProperty(voucherCodeEntity, voucherEntity);
        this.timeCondition = timeConditionEntity;
        this.usageCondition = usageConditionEntity;
        this.storeCondition = storeConditionEntity;
    }

    private void assignProperty(VoucherCodeEntity voucherCodeEntity, VoucherEntity voucherEntity) {
        this.voucherCode = voucherCodeEntity.getVoucherCode();
        this.voucherCodeUsable = voucherCodeEntity.isVoucherCodeUsable();
        this.voucherCodeUsageCount = voucherCodeEntity.getVoucherCodeUsageCount();
        this.customerPhone = voucherCodeEntity.getCustomerPhone();
        this.createdBy = voucherCodeEntity.getCreatedBy();
        this.createdDate = voucherCodeEntity.getCreatedDate();
        this.lastModifiedBy = voucherCodeEntity.getLastModifiedBy();
        this.lastModifiedDate = voucherCodeEntity.getLastModifiedDate();

        this.voucherName = voucherEntity.getVoucherName();
        this.voucherDescription = voucherEntity.getVoucherDescription();
        this.voucherImageUrl = voucherEntity.getVoucherImageUrl();
        this.voucherDiscount = voucherEntity.getVoucherDiscount();
        this.voucherDiscountType = voucherEntity.getVoucherDiscountType();
        this.voucherConditions = voucherEntity.getVoucherConditions();
        this.voucherEnable = voucherEntity.isVoucherEnable();

    }
}
