package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
    private UUID guid;

    @Size(min = 1, max = 16)
    private String productCode;

    @Size(min = 1, max = 100)
    private String productName;

    @Size(max = 2000)
    private String productDescription;

    @Size(max = 255)
    private String productImageUrl;

    @Size(max = 255)
    private String productThumbnailUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    private int productRootPrice;

    private int productNormalPrice;

    private int productSalePrice;

    private UUID categoryGuid;

    public AdminProductDTO() {
    }

    public AdminProductDTO(ProductEntity productEntity) {
        this.guid = productEntity.getGuid();
        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productDescription = productEntity.getProductDescription();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productRootPrice = productEntity.getProductRootPrice();
        this.productNormalPrice = productEntity.getProductNormalPrice();
        this.productSalePrice = productEntity.getProductSalePrice();
        this.categoryGuid = productEntity.getCategoryGuid();

        this.createdBy = productEntity.getCreatedBy();
        this.createdDate = productEntity.getCreatedDate();
        this.lastModifiedBy = productEntity.getLastModifiedBy();
        this.lastModifiedDate = productEntity.getLastModifiedDate();
    }

}
