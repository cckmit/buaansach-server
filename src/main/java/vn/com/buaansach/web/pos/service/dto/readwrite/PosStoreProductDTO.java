package vn.com.buaansach.web.pos.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.ProductType;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;

import java.util.UUID;

@Data
public class PosStoreProductDTO {
    private UUID guid;
    private StoreProductStatus storeProductStatus;
    private boolean storeProductHidden;
    private UUID storeGuid;
    private UUID productGuid;

    private String productCode;
    private String productName;
    private String productUnit;
    private String productDescription;
    private String productImageUrl;
    private String productThumbnailUrl;
    private ProductStatus productStatus;
    private ProductType productType;
    private int productPrice;
    private int productDiscount;
    private DiscountType productDiscountType;
    private int productPosition;
    private UUID saleGuid;

    public PosStoreProductDTO() {
    }

    public PosStoreProductDTO(StoreProductEntity storeProductEntity, ProductEntity productEntity) {
        this.guid = storeProductEntity.getGuid();
        this.storeProductStatus = storeProductEntity.getStoreProductStatus();
        this.storeProductHidden = storeProductEntity.isStoreProductHidden();
        this.storeGuid = storeProductEntity.getStoreGuid();
        this.productGuid = storeProductEntity.getProductGuid();

        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productUnit = productEntity.getProductUnit();
        this.productDescription = productEntity.getProductDescription();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productType = productEntity.getProductType();
        this.productPrice = productEntity.getProductPrice();
        this.productDiscount = productEntity.getProductDiscount();
        this.productDiscountType = productEntity.getProductDiscountType();
        this.productPosition = productEntity.getProductPosition();
        this.saleGuid = productEntity.getSaleGuid();
    }

}
