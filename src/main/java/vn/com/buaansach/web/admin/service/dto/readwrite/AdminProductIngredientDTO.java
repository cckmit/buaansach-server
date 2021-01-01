package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.entity.common.ProductIngredientEntity;

import java.util.List;
import java.util.UUID;

@Data
public class AdminProductIngredientDTO {
    private UUID productGuid;
    private List<ProductIngredientEntity> listProductIngredient;
}
