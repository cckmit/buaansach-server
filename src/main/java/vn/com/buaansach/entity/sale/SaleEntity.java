package vn.com.buaansach.entity.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_sale")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 255)
    @Column(name = "sale_name")
    private String saleName;

    @Size(max = 255)
    @Column(name = "sale_name_eng")
    private String saleNameEng;

    @Size(max = 2000)
    @Column(name = "sale_description", length = 2000)
    private String saleDescription;

    @Size(max = 2000)
    @Column(name = "sale_description_eng", length = 2000)
    private String saleDescriptionEng;

    @Size(max = 255)
    @Column(name = "sale_image_url")
    private String saleImageUrl;

    @Column(name = "sale_discount")
    private int saleDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_discount_type")
    private DiscountType saleDiscountType;

    @Size(max = 500)
    @Column(name = "sale_conditions", length = 500)
    private String saleConditions;

    @Column(name = "sale_activated")
    private boolean saleActivated;
}
