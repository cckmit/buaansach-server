package vn.com.buaansach.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.ProductDisplay;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.ProductType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_product")
@Data
public class ProductEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(min = 1, max = 20)
    @Column(name = "product_code", length = 20)
    private String productCode;

    @Size(min = 1, max = 100)
    @Column(name = "product_name", length = 100)
    private String productName;

    @Column(name = "product_unit", length = 50)
    private String productUnit;

    @Size(max = 2000)
    @Column(name = "product_description", length = 2000)
    private String productDescription;

    @Size(max = 255)
    @Column(name = "product_image_url")
    private String productImageUrl;

    @Size(max = 255)
    @Column(name = "product_thumbnail_url")
    private String productThumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_display")
    private ProductDisplay productDisplay;

    @Column(name = "product_root_price")
    @JsonIgnore
    private int productRootPrice;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "product_position")
    private int productPosition;

    @Column(name = "product_sale_guid")
    private UUID productSaleGuid;
}
