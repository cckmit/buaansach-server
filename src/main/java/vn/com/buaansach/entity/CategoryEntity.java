package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_category")
@Data
public class CategoryEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(min = 1, max = 100)
    @Column(name = "category_name", unique = true, length = 100)
    private String categoryName;

    @Size(max = 1000)
    @Column(name = "category_description", length = 1000)
    private String categoryDescription;

    @Size(max = 255)
    @Column(name = "category_image_url")
    private String categoryImageUrl;
}
