package vn.com.buaansach.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_product_ingredient")
@Data
@NoArgsConstructor
public class ProductIngredientEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_ingredient_amount")
    private int productIngredientAmount;

    /**
     * FK
     */

    @Column(name = "product_guid")
    private UUID productGuid;

    @Column(name = "ingredient_guid")
    private UUID ingredientGuid;


}
