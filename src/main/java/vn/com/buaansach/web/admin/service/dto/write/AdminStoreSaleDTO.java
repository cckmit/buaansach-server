package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.sale.StoreSaleEntity;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AdminStoreSaleDTO {
    private UUID guid;
    private boolean storeSaleActivated;
    private UUID storeGuid;
    private UUID saleGuid;

    public AdminStoreSaleDTO(StoreSaleEntity entity) {
        this.guid = entity.getGuid();
        this.storeSaleActivated = entity.isStoreSaleActivated();
        this.storeGuid = entity.getStoreGuid();
        this.saleGuid = entity.getSaleGuid();
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
