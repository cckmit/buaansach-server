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
@Table(name = "bas_ingredient")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class IngredientEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 100)
    @Column(name = "ingredient_name")
    private String ingredientName;

    @Size(max = 20)
    @Column(name = "ingredient_unit")
    private String ingredientUnit;

    @Size(max = 255)
    @Column(name = "ingredient_image_url")
    private String ingredientImageUrl;

    @Column(name = "ingredient_position")
    private int ingredientPosition;

}
