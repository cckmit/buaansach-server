package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_category")
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(min = 1, max = 50)
    @Column(name = "category_name", unique = true, length = 50)
    private String categoryName;

    @Size(max = 255)
    @Column(name = "category_description")
    private String categoryDescription;

    @Size(max = 255)
    @Column(name = "category_image_url")
    private String categoryImageUrl;
}
