package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.ProductType;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminProductDTO extends AuditDTO {
    private UUID guid;

    @Size(min = 1, max = 20)
    private String productCode;

    @Size(min = 1, max = 255)
    private String productName;

    @Size(min = 1, max = 255)
    private String productNameEng;

    @Size(min = 1, max = 50)
    private String productUnit;

    @Size(min = 1, max = 50)
    private String productUnitEng;

    @Size(max = 2000)
    private String productDescription;

    @Size(max = 2000)
    private String productDescriptionEng;

    @Size(max = 255)
    private String productImageUrl;

    @Size(max = 255)
    private String productThumbnailUrl;

    private ProductStatus productStatus;

    private ProductType productType;

    private int productRootPrice;

    private int productPrice;

    private int productDiscount;

    private DiscountType productDiscountType;

    private int productPosition;

    private boolean productActivated;

    private UUID saleGuid;

    /* Additional */
    private List<CategoryEntity> categories = new ArrayList<>();

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
        this.productNameEng = productEntity.getProductNameEng();
        this.productUnit = productEntity.getProductUnit();
        this.productUnitEng = productEntity.getProductUnitEng();
        this.productDescription = productEntity.getProductDescription();
        this.productDescriptionEng = productEntity.getProductDescriptionEng();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
        this.productStatus = productEntity.getProductStatus();
        this.productType = productEntity.getProductType();
        this.productRootPrice = productEntity.getProductRootPrice();
        this.productPrice = productEntity.getProductPrice();
        this.productDiscount = productEntity.getProductDiscount();
        this.productDiscountType = productEntity.getProductDiscountType();
        this.productPosition = productEntity.getProductPosition();
        this.productActivated = productEntity.isProductActivated();
        this.saleGuid = productEntity.getSaleGuid();

        this.createdBy = productEntity.getCreatedBy();
        this.createdDate = productEntity.getCreatedDate();
        this.lastModifiedBy = productEntity.getLastModifiedBy();
        this.lastModifiedDate = productEntity.getLastModifiedDate();
    }

}
