package vn.com.buaansach.entity.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.SaleType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_sale")
@Data
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

    @Size(max = 1000)
    @Column(name = "sale_description")
    private String saleDescription;

    @Size(max = 255)
    @Column(name = "sale_image_url")
    private String saleImageUrl;

    @Column(name = "sale_amount")
    private int saleAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type")
    private SaleType saleType;

    @Size(max = 500)
    @Column(name = "sale_conditions", length = 500)
    private String saleConditions;

    @Column(name = "sale_enable")
    private boolean saleEnable;
}
