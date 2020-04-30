package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminStoreProductDTO extends AuditDTO {
    private UUID guid;
    private StoreProductStatus storeProductStatus;
    private UUID storeGuid;
    private UUID productGuid;

    private String productName;
    private ProductStatus productStatus;
    private int productPrice;
    private int productDiscount;

    public AdminStoreProductDTO() {
    }

    public AdminStoreProductDTO(StoreProductEntity storeProductEntity, ProductEntity productEntity) {
        this.guid = storeProductEntity.getGuid();
        this.storeProductStatus = storeProductEntity.getStoreProductStatus();
        this.storeGuid = storeProductEntity.getStoreGuid();
        this.productGuid = storeProductEntity.getStoreGuid();

        this.productName = productEntity.getProductName();
        this.productStatus = productEntity.getProductStatus();
        this.productPrice = productEntity.getProductPrice();
        this.productDiscount = productEntity.getProductDiscount();

        this.createdBy = storeProductEntity.getCreatedBy();
        this.createdDate = storeProductEntity.getCreatedDate();
        this.lastModifiedBy = storeProductEntity.getLastModifiedBy();
        this.lastModifiedDate = storeProductEntity.getLastModifiedDate();
    }

    public void updateAudit(StoreProductEntity storeProductEntity) {
        this.guid = storeProductEntity.getGuid();
        this.storeProductStatus = storeProductEntity.getStoreProductStatus();
        this.storeGuid = storeProductEntity.getStoreGuid();
        this.productGuid = storeProductEntity.getStoreGuid();

        this.createdBy = storeProductEntity.getCreatedBy();
        this.createdDate = storeProductEntity.getCreatedDate();
        this.lastModifiedBy = storeProductEntity.getLastModifiedBy();
        this.lastModifiedDate = storeProductEntity.getLastModifiedDate();
    }


}
