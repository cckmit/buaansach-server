package vn.com.buaansach.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;

import java.util.UUID;

@Data
public class ProductForUserDTO {
    private UUID guid;

    private String productCode;

    private String productName;

    private String productDescription;

    private String productImageUrl;

    private String productThumbnailUrl;

    private ProductStatus productStatus;

    private int productPrice;

    public ProductForUserDTO() {
    }

    public ProductForUserDTO(ProductEntity productEntity) {
        this.guid = productEntity.getGuid();
        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productDescription = productEntity.getProductDescription();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productPrice = productEntity.getProductPrice();
    }
}
