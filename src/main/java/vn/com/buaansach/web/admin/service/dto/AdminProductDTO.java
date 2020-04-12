package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.common.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminProductDTO extends AuditDTO {
    private String productCode;

    private String productName;

    private String productDescription;

    private String productImageUrl;

    private String productThumbnailUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    private int productRealPrice;

    private int productPrice;

    private UUID categoryGuid;

    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();
        entity.setGuid(this.guid);
        entity.setProductCode(this.productCode);
        entity.setProductName(this.productName);
        entity.setProductDescription(this.productDescription);
        entity.setProductImageUrl(this.productImageUrl);
        entity.setProductThumbnailUrl(this.productThumbnailUrl);
        entity.setProductStatus(this.productStatus);
        entity.setProductRealPrice(this.productRealPrice);
        entity.setProductPrice(this.productPrice);
        return entity;
    }

}
