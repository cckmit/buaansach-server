package vn.com.buaansach.web.guest.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GuestStoreCategoryDTO {
    private UUID guid;
    private String categoryName;
    private String categoryNameEng;
    private String categoryDescription;
    private String categoryDescriptionEng;
    private String categoryImageUrl;
    private int categoryPosition;

    private List<GuestStoreProductDTO> listStoreProduct = new ArrayList<>();

    public GuestStoreCategoryDTO(CategoryEntity categoryEntity) {
        this.guid = categoryEntity.getGuid();
        this.categoryName = categoryEntity.getCategoryName();
        this.categoryNameEng = categoryEntity.getCategoryNameEng();
        this.categoryDescription = categoryEntity.getCategoryDescription();
        this.categoryDescriptionEng = categoryEntity.getCategoryDescriptionEng();
        this.categoryImageUrl = categoryEntity.getCategoryImageUrl();
        this.categoryPosition = categoryEntity.getCategoryPosition();
    }
}
