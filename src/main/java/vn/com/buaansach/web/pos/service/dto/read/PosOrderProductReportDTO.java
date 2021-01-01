package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.common.ProductIngredientEntity;

import java.util.List;
import java.util.UUID;

@Data
public class PosOrderProductReportDTO {
    private UUID productGuid;
    private ProductEntity product;
    private int numberSold;
    private int numberCancelled;
    private int numberPending;
    private List<ProductIngredientEntity> listProductIngredient;
}
