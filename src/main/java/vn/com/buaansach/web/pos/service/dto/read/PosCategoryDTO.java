package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PosCategoryDTO {
    private UUID guid;
    private String categoryName;
    private String categoryDescription;
    private String categoryImageUrl;
    private int categoryPosition;

    private List<PosStoreProductDTO> listStoreProduct = new ArrayList<>();

    public PosCategoryDTO(CategoryEntity categoryEntity) {
        this.guid = categoryEntity.getGuid();
        this.categoryName = categoryEntity.getCategoryName();
        this.categoryDescription = categoryEntity.getCategoryDescription();
        this.categoryImageUrl = categoryEntity.getCategoryImageUrl();
        this.categoryPosition = categoryEntity.getCategoryPosition();
    }
}
