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

    @Size(min = 1, max = 20)
    @Column(name = "product_code", length = 20)
    private String productCode;

    @Size(min = 1, max = 100)
    @Column(name = "product_name", length = 100)
    private String productName;

    @Size(max = 1000)
    @Column(name = "product_description", length = 1000)
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

    @Column(name = "product_real_price")
    private int productRealPrice;

    @Column(name = "product_price")
    private int productPrice;
}
