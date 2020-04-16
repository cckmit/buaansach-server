package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.CategoryEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminProductDTO extends AuditDTO {
    @Size(min = 1, max = 20)
    private String productCode;

    @Size(min = 1, max = 100)
    private String productName;

    @Size(max = 1000)
    private String productDescription;

    @Size(max = 255)
    private String productImageUrl;

    @Size(max = 255)
    private String productThumbnailUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    private int productRealPrice;

    private int productPrice;

    private UUID categoryGuid;

    private String categoryName;

    public AdminProductDTO() {
    }

    public AdminProductDTO(ProductEntity productEntity, CategoryEntity categoryEntity) {
        this.guid = productEntity.getGuid();
        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productDescription = productEntity.getProductDescription();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productRealPrice = productEntity.getProductRealPrice();
        this.productPrice = productEntity.getProductPrice();
        this.createdBy = productEntity.getCreatedBy();
        this.createdDate = productEntity.getCreatedDate();
        this.lastModifiedBy = productEntity.getLastModifiedBy();
        this.lastModifiedDate = productEntity.getLastModifiedDate();
        if(categoryEntity != null){
            this.categoryGuid = categoryEntity.getGuid();
            this.categoryName = categoryEntity.getCategoryName();
        }
    }

}
