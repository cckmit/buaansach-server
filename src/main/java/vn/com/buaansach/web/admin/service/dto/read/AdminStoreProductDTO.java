package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.ProductType;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.shared.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminStoreProductDTO extends AuditDTO {
    private UUID guid;
    private StoreProductStatus storeProductStatus;
    private boolean storeProductHidden;
    private UUID storeGuid;
    private UUID productGuid;

    private String productCode;
    private String productName;
    private String productNameEng;
    private String productUnit;
    private String productUnitEng;
    private String productDescription;
    private String productDescriptionEng;
    private String productImageUrl;
    private String productThumbnailUrl;
    private ProductStatus productStatus;
    private ProductType productType;
    private int productPrice;
    private int productDiscount;
    private DiscountType productDiscountType;
    private int productPosition;
    private boolean productActivated;
    private UUID saleGuid;

    public AdminStoreProductDTO() {
    }

    public AdminStoreProductDTO(StoreProductEntity storeProductEntity, ProductEntity productEntity) {
        this.guid = storeProductEntity.getGuid();
        this.storeProductStatus = storeProductEntity.getStoreProductStatus();
        this.storeProductHidden = storeProductEntity.isStoreProductHidden();
        this.storeGuid = storeProductEntity.getStoreGuid();
        this.productGuid = storeProductEntity.getStoreGuid();

        this.createdBy = storeProductEntity.getCreatedBy();
        this.createdDate = storeProductEntity.getCreatedDate();
        this.lastModifiedBy = storeProductEntity.getLastModifiedBy();
        this.lastModifiedDate = storeProductEntity.getLastModifiedDate();

        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productNameEng = productEntity.getProductNameEng();
        this.productUnit = productEntity.getProductUnit();
        this.productUnitEng = productEntity.getProductUnitEng();
        this.productDescription = productEntity.getProductDescription();
        this.productDescriptionEng = productEntity.getProductDescriptionEng();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productType = productEntity.getProductType();
        this.productPrice = productEntity.getProductPrice();
        this.productDiscount = productEntity.getProductDiscount();
        this.productDiscountType = productEntity.getProductDiscountType();
        this.productPosition = productEntity.getProductPosition();
        this.productActivated = productEntity.isProductActivated();
        this.saleGuid = productEntity.getSaleGuid();
    }

    public void updateAudit(StoreProductEntity storeProductEntity) {
        this.guid = storeProductEntity.getGuid();
        this.storeProductStatus = storeProductEntity.getStoreProductStatus();
        this.storeProductHidden = storeProductEntity.isStoreProductHidden();
        this.storeGuid = storeProductEntity.getStoreGuid();
        this.productGuid = storeProductEntity.getStoreGuid();

        this.createdBy = storeProductEntity.getCreatedBy();
        this.createdDate = storeProductEntity.getCreatedDate();
        this.lastModifiedBy = storeProductEntity.getLastModifiedBy();
        this.lastModifiedDate = storeProductEntity.getLastModifiedDate();
    }


}
