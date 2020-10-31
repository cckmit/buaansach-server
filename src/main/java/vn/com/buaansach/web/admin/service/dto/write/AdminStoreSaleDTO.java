package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.entity.sale.StoreSaleEntity;
import vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AdminStoreSaleDTO {
    private UUID guid;
    private boolean storeSaleActivated;
    private UUID storeGuid;
    private UUID saleGuid;

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

    public AdminStoreSaleDTO(StoreSaleEntity entity) {
        this.guid = entity.getGuid();
        this.storeSaleActivated = entity.isStoreSaleActivated();
        this.storeGuid = entity.getStoreGuid();
        this.saleGuid = entity.getSaleGuid();
    }

    public AdminStoreSaleDTO(StoreSaleEntity entity, SaleEntity saleEntity, SaleTimeConditionEntity timeCondition) {
        this.guid = entity.getGuid();
        this.storeSaleActivated = entity.isStoreSaleActivated();
        this.storeGuid = entity.getStoreGuid();
        this.saleGuid = entity.getSaleGuid();

        this.saleName = saleEntity.getSaleName();
        this.saleNameEng = saleEntity.getSaleNameEng();
        this.saleDescription = saleEntity.getSaleDescription();
        this.saleDescriptionEng = saleEntity.getSaleDescriptionEng();
        this.saleImageUrl = saleEntity.getSaleImageUrl();
        this.saleDiscount = saleEntity.getSaleDiscount();
        this.saleDiscountType = saleEntity.getSaleDiscountType();
        this.saleConditions = saleEntity.getSaleConditions();
        this.saleActivated = saleEntity.isSaleActivated();

        this.timeCondition = timeCondition;
    }

    public StoreSaleEntity toEntity() {
        StoreSaleEntity entity = new StoreSaleEntity();
        entity.setGuid(this.guid);
        entity.setStoreSaleActivated(this.storeSaleActivated);
        entity.setStoreGuid(this.storeGuid);
        entity.setSaleGuid(this.saleGuid);
        return entity;
    }
}
