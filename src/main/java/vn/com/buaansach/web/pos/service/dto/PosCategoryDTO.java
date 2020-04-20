package vn.com.buaansach.web.pos.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.CategoryEntity;

import java.util.UUID;

@Data
public class PosCategoryDTO {
    private UUID guid;
    private String categoryName;
    private String categoryDescription;
    private String categoryImageUrl;

    public PosCategoryDTO(CategoryEntity categoryEntity) {
        this.guid = categoryEntity.getGuid();
        this.categoryName = categoryEntity.getCategoryName();
        this.categoryDescription = categoryEntity.getCategoryDescription();
        this.categoryImageUrl = categoryEntity.getCategoryImageUrl();
    }
}
