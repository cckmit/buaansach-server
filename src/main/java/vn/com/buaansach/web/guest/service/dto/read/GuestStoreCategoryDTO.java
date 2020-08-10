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
    private String categoryDescription;
    private String categoryImageUrl;
    private int categoryPosition;

    private List<GuestStoreProductDTO> listStoreProduct = new ArrayList<>();

    public GuestStoreCategoryDTO(CategoryEntity categoryEntity) {
        this.guid = categoryEntity.getGuid();
        this.categoryName = categoryEntity.getCategoryName();
        this.categoryDescription = categoryEntity.getCategoryDescription();
        this.categoryImageUrl = categoryEntity.getCategoryImageUrl();
        this.categoryPosition = categoryEntity.getCategoryPosition();
    }
}
