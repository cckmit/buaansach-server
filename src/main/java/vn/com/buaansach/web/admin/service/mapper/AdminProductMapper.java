package vn.com.buaansach.web.admin.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminProductDTO;

@Service
public class AdminProductMapper {
    public ProductEntity dtoToEntity(AdminProductDTO dto) {
        if (dto == null) return null;
        ProductEntity entity = new ProductEntity();
        entity.setGuid(dto.getGuid());
        entity.setProductCode(dto.getProductCode());
        entity.setProductName(dto.getProductName());
        entity.setProductNameEng(dto.getProductNameEng());
        entity.setProductUnit(dto.getProductUnit());
        entity.setProductUnitEng(dto.getProductUnitEng());
        entity.setProductDescription(dto.getProductDescription());
        entity.setProductDescriptionEng(dto.getProductDescriptionEng());
        entity.setProductImageUrl(dto.getProductImageUrl());
        entity.setProductThumbnailUrl(dto.getProductThumbnailUrl());
        entity.setProductStatus(dto.getProductStatus());
        entity.setProductType(dto.getProductType());
        entity.setProductRootPrice(dto.getProductRootPrice());
        entity.setProductPrice(dto.getProductPrice());
        entity.setProductDiscount(dto.getProductDiscount());
        entity.setProductDiscountType(dto.getProductDiscountType());
        entity.setProductPosition(dto.getProductPosition());
        entity.setProductActivated(dto.isProductActivated());
        entity.setSaleGuid(dto.getSaleGuid());
        return entity;
    }
}
