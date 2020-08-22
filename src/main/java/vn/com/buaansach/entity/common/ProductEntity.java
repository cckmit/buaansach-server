package vn.com.buaansach.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
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

    @Size(max = 20)
    @Column(name = "product_code", length = 20)
    private String productCode;

    @Size(max = 255)
    @Column(name = "product_name")
    private String productName;

    @Size(max = 255)
    @Column(name = "product_name_eng")
    private String productNameEng;

    @Column(name = "product_unit", length = 50)
    private String productUnit;

    @Column(name = "product_unit_eng", length = 50)
    private String productUnitEng;

    @Size(max = 2000)
    @Column(name = "product_description", length = 2000)
    private String productDescription;

    @Size(max = 2000)
    @Column(name = "product_description_eng", length = 2000)
    private String productDescriptionEng;

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

    @Column(name = "product_root_price")
    private int productRootPrice;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "product_discount")
    private int productDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_discount_type")
    private DiscountType productDiscountType;

    @Column(name = "product_position")
    private int productPosition;

    @Column(name = "hide_product")
    private boolean hideProduct;

    /**
     * FK
     */

    @Column(name = "sale_guid")
    private UUID saleGuid;
}
