package vn.com.buaansach.web.admin.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.CategoryEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.web.admin.service.dto.AdminProductDTO;

@Service
public class AdminProductMapper {

    public AdminProductDTO entityToDTO(ProductEntity productEntity, CategoryEntity categoryEntity) {
        return new AdminProductDTO(productEntity, categoryEntity);
    }

    public ProductEntity dtoToEntity(AdminProductDTO dto) {
        if (dto == null) {
            return null;
        } else {
            ProductEntity entity = new ProductEntity();
            entity.setGuid(dto.getGuid());
            entity.setProductCode(dto.getProductCode());
            entity.setProductName(dto.getProductName());
            entity.setProductDescription(dto.getProductDescription());
            entity.setProductImageUrl(dto.getProductImageUrl());
            entity.setProductThumbnailUrl(dto.getProductThumbnailUrl());
            entity.setProductStatus(dto.getProductStatus());
            entity.setProductRealPrice(dto.getProductRealPrice());
            entity.setProductPrice(dto.getProductPrice());
            return entity;
        }
    }
}
