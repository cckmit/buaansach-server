package vn.com.buaansach.web.guest.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.StoreProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;

import java.util.UUID;

@Data
public class GuestProductDTO {
    private UUID guid;
    private UUID storeGuid;
    private UUID productGuid;
    private StoreProductStatus storeProductStatus;

    private String productCode;
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productThumbnailUrl;
    private ProductStatus productStatus;
    private int productNormalPrice;
    private UUID categoryGuid;

    public GuestProductDTO() {
    }

    public GuestProductDTO(StoreProductEntity storeProductEntity, ProductEntity productEntity) {
        this.guid = storeProductEntity.getGuid();
        this.storeProductStatus = storeProductEntity.getStoreProductStatus();
        this.storeGuid = storeProductEntity.getStoreGuid();
        this.productGuid = storeProductEntity.getProductGuid();

        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productDescription = productEntity.getProductDescription();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productNormalPrice = productEntity.getProductNormalPrice();
        this.categoryGuid = productEntity.getCategoryGuid();
    }
}
