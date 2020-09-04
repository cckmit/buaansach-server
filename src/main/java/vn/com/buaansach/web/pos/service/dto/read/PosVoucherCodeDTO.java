package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class PosVoucherCodeDTO extends AuditDTO {
    private String voucherCode;
    private boolean voucherCodeActivated;
    private int voucherCodeUsageCount;
    private String voucherCodePhone;
    private UUID voucherGuid;

    private String voucherName;
    private String voucherDescription;
    private String voucherImageUrl;
    private int voucherDiscount;
    private DiscountType voucherDiscountType;
    private String voucherConditions;
    private boolean voucherActivated;

    private VoucherTimeConditionEntity timeCondition;
    private VoucherUsageConditionEntity usageCondition;

    public PosVoucherCodeDTO() {
    }

    public PosVoucherCodeDTO(VoucherCodeEntity voucherCodeEntity, VoucherEntity voucherEntity) {
        assignProperty(voucherCodeEntity, voucherEntity);
    }

    public PosVoucherCodeDTO(VoucherCodeEntity voucherCodeEntity, VoucherEntity voucherEntity,
                             VoucherTimeConditionEntity timeConditionEntity,
                             VoucherUsageConditionEntity usageConditionEntity) {
        assignProperty(voucherCodeEntity, voucherEntity);
        this.timeCondition = timeConditionEntity;
        this.usageCondition = usageConditionEntity;
    }

    private void assignProperty(VoucherCodeEntity voucherCodeEntity, VoucherEntity voucherEntity) {
        this.voucherCode = voucherCodeEntity.getVoucherCode();
        this.voucherCodeActivated = voucherCodeEntity.isVoucherCodeActivated();
        this.voucherCodeUsageCount = voucherCodeEntity.getVoucherCodeUsageCount();
        this.voucherCodePhone = voucherCodeEntity.getVoucherCodePhone();
        this.voucherGuid = voucherCodeEntity.getVoucherGuid();

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
        this.voucherActivated = voucherEntity.isVoucherActivated();

    }
}
