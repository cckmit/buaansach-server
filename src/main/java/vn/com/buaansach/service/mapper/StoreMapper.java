package vn.com.buaansach.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.service.dto.StoreDTO;

@Service
public class StoreMapper {
    public StoreDTO storeEntityToStoreDTO(StoreEntity entity) {
        return new StoreDTO(entity);
    }

    public StoreEntity storeDTOToStoreEntity(StoreDTO dto) {
        if (dto == null) {
            return null;
        } else {
            StoreEntity entity = new StoreEntity();
            entity.setGuid(dto.getGuid());
            entity.setStoreCode(dto.getStoreCode());
            entity.setStoreName(dto.getStoreName());
            entity.setStoreAddress(dto.getStoreAddress());
            entity.setStoreImageUrl(dto.getStoreImageUrl());
            entity.setStoreStatus(dto.getStoreStatus());
            entity.setStoreOwnerName(dto.getStoreOwnerName());
            entity.setStoreOwnerPhone(dto.getStoreOwnerPhone());
            entity.setStoreOwnerEmail(dto.getStoreOwnerEmail());
            entity.setStoreTaxCode(dto.getStoreTaxCode());
            entity.setLastUpdateReason(dto.getLastUpdateReason());
            entity.setCreatedBy(dto.getCreatedBy());
            entity.setCreatedDate(dto.getCreatedDate());
            entity.setLastModifiedBy(dto.getLastModifiedBy());
            entity.setLastModifiedDate(dto.getLastModifiedDate());
            return entity;
        }
    }

}
