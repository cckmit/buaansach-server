package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.common.CategoryEntity;

import java.util.UUID;

@Data
public class PosCategoryDTO {
    private UUID guid;
    private String categoryName;
    private String categoryDescription;
    private String categoryImageUrl;
    private int categoryPosition;

    public PosCategoryDTO(CategoryEntity categoryEntity) {
        this.guid = categoryEntity.getGuid();
        this.categoryName = categoryEntity.getCategoryName();
        this.categoryDescription = categoryEntity.getCategoryDescription();
        this.categoryImageUrl = categoryEntity.getCategoryImageUrl();
        this.categoryPosition = categoryEntity.getCategoryPosition();
    }
}
