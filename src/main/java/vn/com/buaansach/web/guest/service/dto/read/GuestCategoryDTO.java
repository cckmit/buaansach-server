package vn.com.buaansach.web.guest.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.CategoryEntity;

import java.util.UUID;

@Data
public class GuestCategoryDTO {
    private UUID guid;
    private String categoryName;
    private String categoryDescription;
    private String categoryImageUrl;

    public GuestCategoryDTO(CategoryEntity categoryEntity) {
        this.guid = categoryEntity.getGuid();
        this.categoryName = categoryEntity.getCategoryName();
        this.categoryDescription = categoryEntity.getCategoryDescription();
        this.categoryImageUrl = categoryEntity.getCategoryImageUrl();
    }
}
