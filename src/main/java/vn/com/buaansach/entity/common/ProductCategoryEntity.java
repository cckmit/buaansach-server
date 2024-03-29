package vn.com.buaansach.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_product_category")
@Data
@NoArgsConstructor
public class ProductCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * FK
     */

    @Column(name = "product_guid")
    private UUID productGuid;

    @Column(name = "category_guid")
    private UUID categoryGuid;
}
