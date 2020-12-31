package vn.com.buaansach.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_recipe")
@Data
@NoArgsConstructor
public class RecipeEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "recipe_description")
    private String recipeDescription;

    @Column(name = "recipe_image_url")
    private String recipeImageUrl;

    @Column(name = "recipe_activated")
    private boolean recipeActivated;

    @Column(name = "recipe_position")
    private int recipePosition;
}
