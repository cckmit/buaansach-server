package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.ProductStatus;

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

    @Size(min = 1, max = 16)
    @Column(name = "product_code", length = 16)
    private String productCode;

    @Size(min = 1, max = 100)
    @Column(name = "product_name", length = 100)
    private String productName;

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

    @Column(name = "product_root_price")
    private int productRootPrice;

    @Column(name = "product_normal_price")
    private int productNormalPrice;

    @Column(name = "product_sale_price")
    private int productSalePrice;

    @Column(name = "category_guid")
    private UUID categoryGuid;
}
