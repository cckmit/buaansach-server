package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.StoreProductEntity;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.repository.AdminProductRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreProductRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminStoreProductService {
    private final AdminStoreProductRepository adminStoreProductRepository;
    private final AdminProductRepository adminProductRepository;
    private final AdminStoreRepository adminStoreRepository;

    public AdminStoreProductService(AdminStoreProductRepository adminStoreProductRepository, AdminProductRepository adminProductRepository, AdminStoreRepository adminStoreRepository) {
        this.adminStoreProductRepository = adminStoreProductRepository;
        this.adminProductRepository = adminProductRepository;
        this.adminStoreRepository = adminStoreRepository;
    }


    public AdminStoreProductDTO addProductToStore(AdminStoreProductDTO payload) {
        adminStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + payload.getStoreGuid()));

        ProductEntity productEntity = adminProductRepository.findOneByGuid(payload.getProductGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with guid: " + payload.getProductGuid()));

        StoreProductEntity storeProductEntity = new StoreProductEntity();
        storeProductEntity.setGuid(UUID.randomUUID());
        storeProductEntity.setStoreProductStatus(StoreProductStatus.AVAILABLE);
        storeProductEntity.setStoreGuid(payload.getStoreGuid());
        storeProductEntity.setProductGuid(payload.getProductGuid());
        return new AdminStoreProductDTO(adminStoreProductRepository.save(storeProductEntity), productEntity);
    }

    public List<AdminStoreProductDTO> addAllProductToStore(String storeGuid) {
        List<ProductEntity> listProduct = adminProductRepository.findAllProductNotInStore(UUID.fromString(storeGuid));
        List<StoreProductEntity> listStoreProduct = new ArrayList<>();
        listProduct.forEach(productEntity -> {
            StoreProductEntity storeProductEntity = new StoreProductEntity();
            storeProductEntity.setGuid(UUID.randomUUID());
            storeProductEntity.setStoreProductStatus(StoreProductStatus.AVAILABLE);
            storeProductEntity.setProductGuid(productEntity.getGuid());
            storeProductEntity.setStoreGuid(UUID.fromString(storeGuid));
            listStoreProduct.add(storeProductEntity);
        });
        adminStoreProductRepository.saveAll(listStoreProduct);
        return getListStoreProductByStoreGuid(storeGuid);
    }

    public AdminStoreProductDTO updateStoreProduct(AdminStoreProductDTO payload) {

        StoreProductEntity storeProductEntity = adminStoreProductRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store Product not found with guid: " + payload.getGuid()));

        storeProductEntity.setStoreProductStatus(payload.getStoreProductStatus());
        payload.updateAudit(adminStoreProductRepository.save(storeProductEntity));
        return payload;
    }

    public List<AdminStoreProductDTO> getListStoreProductByStoreGuid(String storeGuid) {
        return adminStoreProductRepository.findListAdminStoreProductDTO(UUID.fromString(storeGuid));
    }

    public void deleteStoreProduct(String storeProductGuid) {
        StoreProductEntity storeProductEntity = adminStoreProductRepository
                .findOneByGuid(UUID.fromString(storeProductGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store Product not found with guid: " + storeProductGuid));
        adminStoreProductRepository.delete(storeProductEntity);
    }

}
