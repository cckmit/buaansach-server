package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.shared.service.dto.AuditDTO;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity;

import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminVoucherDTO extends AuditDTO {
    private UUID guid;

    @Size(max = 255)
    private String voucherName;

    @Size(max = 255)
    private String voucherNameEng;

    @Size(max = 2000)
    private String voucherDescription;

    @Size(max = 2000)
    private String voucherDescriptionEng;

    @Size(max = 255)
    private String voucherImageUrl;

    private int voucherDiscount;

    private DiscountType voucherDiscountType;

    @Size(max = 500)
    private String voucherConditions;

    private boolean voucherActivated;

    private int numberVoucherCode;

    /* computed */
    private VoucherTimeConditionEntity timeCondition;
    private VoucherUsageConditionEntity usageCondition;

    public AdminVoucherDTO() {
    }

    public AdminVoucherDTO(VoucherEntity entity) {
        assignProperty(entity);
    }

    public AdminVoucherDTO(VoucherEntity entity, VoucherTimeConditionEntity timeCondition, VoucherUsageConditionEntity usageCondition) {
        assignProperty(entity);
        this.timeCondition = timeCondition;
        this.usageCondition = usageCondition;
    }

    private void assignProperty(VoucherEntity entity) {
        this.guid = entity.getGuid();
        this.voucherName = entity.getVoucherName();
        this.voucherNameEng = entity.getVoucherNameEng();
        this.voucherDescription = entity.getVoucherDescription();
        this.voucherDescriptionEng = entity.getVoucherDescriptionEng();
        this.voucherImageUrl = entity.getVoucherImageUrl();
        this.voucherDiscount = entity.getVoucherDiscount();
        this.voucherDiscountType = entity.getVoucherDiscountType();
        this.voucherConditions = entity.getVoucherConditions();
        this.voucherActivated = entity.isVoucherActivated();
        this.numberVoucherCode = entity.getNumberVoucherCode();

        this.createdBy = entity.getCreatedBy();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
