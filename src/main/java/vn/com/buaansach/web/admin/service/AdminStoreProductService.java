package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.common.AdminProductRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreProductRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminStoreProductService {
    private final AdminStoreProductRepository adminStoreProductRepository;
    private final AdminProductRepository adminProductRepository;
    private final AdminStoreRepository adminStoreRepository;

    public AdminStoreProductDTO addProductToStore(AdminStoreProductDTO payload) {
        adminStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        ProductEntity productEntity = adminProductRepository.findOneByGuid(payload.getProductGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        StoreProductEntity storeProductEntity = new StoreProductEntity();
        storeProductEntity.setGuid(UUID.randomUUID());
        storeProductEntity.setStoreProductStatus(StoreProductStatus.AVAILABLE);
        storeProductEntity.setStoreProductHidden(payload.isStoreProductHidden());
        storeProductEntity.setStoreGuid(payload.getStoreGuid());
        storeProductEntity.setProductGuid(payload.getProductGuid());
        return new AdminStoreProductDTO(adminStoreProductRepository.save(storeProductEntity), productEntity);
    }

    public List<AdminStoreProductDTO> addAllProductToStore(String storeGuid) {
        adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        List<ProductEntity> listProduct = adminProductRepository.findAllProductNotInStoreExceptStatus(UUID.fromString(storeGuid), ProductStatus.STOP_TRADING);
        List<StoreProductEntity> listStoreProduct = new ArrayList<>();
        listProduct.forEach(productEntity -> {
            StoreProductEntity storeProductEntity = new StoreProductEntity();
            storeProductEntity.setGuid(UUID.randomUUID());
            storeProductEntity.setStoreProductStatus(StoreProductStatus.AVAILABLE);
            storeProductEntity.setStoreProductHidden(false);
            storeProductEntity.setProductGuid(productEntity.getGuid());
            storeProductEntity.setStoreGuid(UUID.fromString(storeGuid));
            listStoreProduct.add(storeProductEntity);
        });
        adminStoreProductRepository.saveAll(listStoreProduct);
        return getListStoreProductByStoreGuid(storeGuid);
    }

    public AdminStoreProductDTO updateStoreProduct(AdminStoreProductDTO payload) {

        StoreProductEntity storeProductEntity = adminStoreProductRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_PRODUCT_NOT_FOUND));

        storeProductEntity.setStoreProductStatus(payload.getStoreProductStatus());
        storeProductEntity.setStoreProductHidden(payload.isStoreProductHidden());
        payload.updateAudit(adminStoreProductRepository.save(storeProductEntity));
        return payload;
    }

    public List<AdminStoreProductDTO> getListStoreProductByStoreGuid(String storeGuid) {
        return adminStoreProductRepository.findListAdminStoreProductDTOByStore(UUID.fromString(storeGuid));
    }

    public void deleteStoreProduct(String storeProductGuid) {
        StoreProductEntity storeProductEntity = adminStoreProductRepository
                .findOneByGuid(UUID.fromString(storeProductGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_PRODUCT_NOT_FOUND));
        adminStoreProductRepository.delete(storeProductEntity);
    }

}
