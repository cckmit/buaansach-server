package vn.com.buaansach.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_category")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 100)
    @Column(name = "category_name", unique = true, length = 100)
    private String categoryName;

    @Size(max = 100)
    @Column(name = "category_name_eng", unique = true, length = 100)
    private String categoryNameEng;

    @Size(max = 255)
    @Column(name = "category_description")
    private String categoryDescription;

    @Size(max = 255)
    @Column(name = "category_description_eng")
    private String categoryDescriptionEng;

    @Size(max = 255)
    @Column(name = "category_image_url")
    private String categoryImageUrl;

    @Size(max = 255)
    @Column(name = "category_thumbnail_url")
    private String categoryThumbnailUrl;

    @Column(name = "category_position")
    private int categoryPosition;

    /* Ẩn đối với trang của khách, vẫn hiện trên trang POS */
    @Column(name = "category_activated")
    private boolean categoryActivated;
}
