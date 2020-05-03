package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.VoucherDiscountType;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherStoreConditionEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminVoucherDTO extends AuditDTO {
    private UUID guid;

    @Size(max = 50)
    private String voucherName;

    @Size(max = 255)
    private String voucherDescription;

    @Size(max = 255)
    private String voucherImageUrl;

    private int voucherDiscount;

    @Enumerated(EnumType.STRING)
    private VoucherDiscountType voucherDiscountType;

    @Size(max = 500)
    private String voucherConditions;

    private boolean voucherEnable;

    private int numberVoucherCode;

    /* computed */
    private VoucherTimeConditionEntity timeCondition;
    private VoucherUsageConditionEntity usageCondition;
    private VoucherStoreConditionEntity storeCondition;

    public AdminVoucherDTO() {
    }

    public AdminVoucherDTO(VoucherEntity entity) {
        assignProperty(entity);
    }

    public AdminVoucherDTO(VoucherEntity entity, VoucherTimeConditionEntity timeCondition, VoucherUsageConditionEntity usageCondition, VoucherStoreConditionEntity storeCondition) {
        assignProperty(entity);
        this.timeCondition = timeCondition;
        this.usageCondition = usageCondition;
        this.storeCondition = storeCondition;
    }

    private void assignProperty(VoucherEntity entity) {
        this.guid = entity.getGuid();
        this.voucherName = entity.getVoucherName();
        this.voucherDescription = entity.getVoucherDescription();
        this.voucherImageUrl = entity.getVoucherImageUrl();
        this.voucherDiscount = entity.getVoucherDiscount();
        this.voucherDiscountType = entity.getVoucherDiscountType();
        this.voucherConditions = entity.getVoucherConditions();
        this.voucherEnable = entity.isVoucherEnable();
        this.numberVoucherCode = entity.getNumberVoucherCode();
        this.createdBy = entity.getCreatedBy();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
