package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PosSaleDTO extends AuditDTO {
    private UUID guid;
    private String saleName;
    private String saleNameEng;
    private String saleDescription;
    private String saleDescriptionEng;
    private String saleImageUrl;
    private int saleDiscount;
    private DiscountType saleDiscountType;
    private String saleConditions;
    private boolean saleActivated;

    private SaleTimeConditionEntity timeCondition;

    public PosSaleDTO(SaleEntity entity) {
        updateAttribute(entity);
    }

    public PosSaleDTO(SaleEntity entity, SaleTimeConditionEntity timeCondition) {
        updateAttribute(entity);
        this.timeCondition = timeCondition;
    }

    private void updateAttribute(SaleEntity entity) {
        this.guid = entity.getGuid();
        this.saleName = entity.getSaleName();
        this.saleNameEng = entity.getSaleNameEng();
        this.saleDescription = entity.getSaleDescription();
        this.saleDescriptionEng = entity.getSaleDescriptionEng();
        this.saleImageUrl = entity.getSaleImageUrl();
        this.saleDiscount = entity.getSaleDiscount();
        this.saleDiscountType = entity.getSaleDiscountType();
        this.saleConditions = entity.getSaleConditions();
        this.saleActivated = entity.isSaleActivated();

        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }

    public SaleEntity toEntity() {
        SaleEntity entity = new SaleEntity();
        entity.setGuid(this.guid);
        entity.setSaleName(this.saleName);
        entity.setSaleNameEng(this.saleNameEng);
        entity.setSaleDescription(this.saleDescription);
        entity.setSaleDescriptionEng(this.saleDescriptionEng);
        entity.setSaleImageUrl(this.saleImageUrl);
        entity.setSaleDiscount(this.saleDiscount);
        entity.setSaleDiscountType(this.saleDiscountType);
        entity.setSaleConditions(this.saleConditions);
        entity.setSaleActivated(this.saleActivated);
        return entity;
    }
}
