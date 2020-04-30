package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.List;
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

    private int productPrice;

    private int productDiscount;

    private List<CategoryEntity> categories;

    public AdminProductDTO() {
    }

    public AdminProductDTO(ProductEntity productEntity) {
        assignProperty(productEntity);
    }

    public AdminProductDTO(ProductEntity productEntity, List<CategoryEntity> categories) {
        assignProperty(productEntity);
        this.categories = categories;
    }

    private void assignProperty(ProductEntity productEntity) {
        this.guid = productEntity.getGuid();
        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productDescription = productEntity.getProductDescription();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productRootPrice = productEntity.getProductRootPrice();
        this.productPrice = productEntity.getProductPrice();
        this.productDiscount = productEntity.getProductDiscount();

        this.createdBy = productEntity.getCreatedBy();
        this.createdDate = productEntity.getCreatedDate();
        this.lastModifiedBy = productEntity.getLastModifiedBy();
        this.lastModifiedDate = productEntity.getLastModifiedDate();
    }

}
