package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.entity.common.ProductCategoryEntity;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.common.AdminCategoryRepository;
import vn.com.buaansach.web.admin.repository.common.AdminProductCategoryRepository;
import vn.com.buaansach.web.admin.repository.common.AdminProductRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreProductRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminProductDTO;
import vn.com.buaansach.web.admin.service.mapper.AdminProductMapper;
import vn.com.buaansach.web.common.service.FileService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final AdminProductRepository adminProductRepository;
    private final AdminStoreProductRepository adminStoreProductRepository;
    private final FileService fileService;
    private final AdminProductMapper adminProductMapper;
    private final AdminProductCategoryRepository adminProductCategoryRepository;
    private final AdminCategoryRepository adminCategoryRepository;
    private final AdminCodeService adminCodeService;

    private void saveProductCategory(UUID productGuid, List<CategoryEntity> categories) {
        List<ProductCategoryEntity> productCategories = categories.stream().map(categoryEntity -> {
            ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
            productCategoryEntity.setProductGuid(productGuid);
            productCategoryEntity.setCategoryGuid(categoryEntity.getGuid());
            return productCategoryEntity;
        }).collect(Collectors.toList());
        adminProductCategoryRepository.saveAll(productCategories);
    }

    @Transactional
    public AdminProductDTO createProduct(AdminProductDTO payload, MultipartFile image) {
        ProductEntity productEntity = adminProductMapper.dtoToEntity(payload);

        if (adminProductRepository.findOneByProductCode(productEntity.getProductCode()).isPresent()) {
            throw new BadRequestException("admin@productCodeExist@" + productEntity.getProductCode());
        }
        Integer lastPos = adminProductRepository.findLastProductPosition();
        int pos = lastPos != null ? lastPos + Constants.POSITION_INCREMENT : Constants.POSITION_INCREMENT - 1;
        UUID productGuid = UUID.randomUUID();
        productEntity.setGuid(productGuid);
        productEntity.setProductCode(adminCodeService.generateCodeForProduct());
        productEntity.setProductPosition(pos);
        saveProductCategory(productGuid, payload.getCategories());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            productEntity.setProductThumbnailUrl(fileEntity.getUrl());
        }
        return new AdminProductDTO(adminProductRepository.save(productEntity), payload.getCategories());
    }

    public AdminProductDTO getProduct(String productGuid) {
        List<CategoryEntity> categories = adminCategoryRepository.findListCategoryByProductGuid(UUID.fromString(productGuid));
        ProductEntity productEntity = adminProductRepository.findOneByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new NotFoundException("admin@productNotFound@" + productGuid));
        return new AdminProductDTO(productEntity, categories);
    }

    @Transactional
    public AdminProductDTO updateProduct(AdminProductDTO payload, MultipartFile image) {
        ProductEntity updateEntity = adminProductMapper.dtoToEntity(payload);

        ProductEntity currentEntity = adminProductRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new NotFoundException("admin@productNotFound@" + updateEntity.getGuid()));

        /* Delete all product category first */
        adminProductCategoryRepository.deleteByProductGuid(updateEntity.getGuid());
        /* Then re-create base on new list */
        saveProductCategory(updateEntity.getGuid(), payload.getCategories());

        /* allow change product code */
//        String updateProductCode = updateEntity.getProductCode().toLowerCase();
//        String currentProductCode = currentEntity.getProductCode().toLowerCase();
//        /* Change product code, check if code has been used or not */
//        if (!updateProductCode.equals(currentProductCode)) {
//            if (adminProductRepository.findOneByProductCode(updateEntity.getProductCode()).isPresent()) {
//                throw new BadRequestException("Product Code already in use");
//            }
//        }

        /* do not allow change product code */
        updateEntity.setProductCode(currentEntity.getProductCode());

        /* if product is stop trading => delete all store product */
        if (updateEntity.getProductStatus().equals(ProductStatus.STOP_TRADING)) {
            List<StoreProductEntity> listStoreProduct = adminStoreProductRepository.findByProductGuid(updateEntity.getGuid());
            adminStoreProductRepository.deleteInBatch(listStoreProduct);

//            listStoreProduct = listStoreProduct.stream()
//                    .peek(storeProductEntity -> storeProductEntity.setStoreProductStatus(StoreProductStatus.STOP_TRADING))
//                    .collect(Collectors.toList());
//            adminStoreProductRepository.saveAll(listStoreProduct);
        }

        if (image != null) {
            /* if update image */
            fileService.deleteByUrl(currentEntity.getProductThumbnailUrl());
            FileEntity newImage = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            updateEntity.setProductThumbnailUrl(newImage.getUrl());
        } else {
            /* if updateEntity has imageUrl = null or empty
             * => user has cleared image
             * => delete currentEntity's image
             */
            if (updateEntity.getProductThumbnailUrl() == null || updateEntity.getProductThumbnailUrl().isEmpty()) {
                fileService.deleteByUrl(currentEntity.getProductThumbnailUrl());
            }
        }

        /* set id to perform update */
        updateEntity.setId(currentEntity.getId());
        return new AdminProductDTO(adminProductRepository.save(updateEntity), payload.getCategories());
    }

    public Page<AdminProductDTO> getPageProduct(PageRequest request, String search) {
        Page<ProductEntity> pageProductEntity = adminProductRepository.findPageProductWithKeyword(request, search.toLowerCase());
        return pageProductEntity.map(productEntity -> {
            List<CategoryEntity> categories = adminCategoryRepository.findListCategoryByProductGuid(productEntity.getGuid());
            return new AdminProductDTO(productEntity, categories);
        });
    }

    public List<ProductEntity> getListProductNotInStore(String storeGuid) {
        return adminProductRepository.findAllProductNotInStoreExcept(UUID.fromString(storeGuid), ProductStatus.STOP_TRADING);
    }

    @Transactional
    public void deleteProduct(String productGuid) {
        ProductEntity productEntity = adminProductRepository.findOneByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new NotFoundException("admin@productNotFound@" + productGuid));
        fileService.deleteByUrl(productEntity.getProductImageUrl());
        fileService.deleteByUrl(productEntity.getProductThumbnailUrl());

        adminProductCategoryRepository.deleteByProductGuid(UUID.fromString(productGuid));
        /* delete all store product before delete product */
        List<StoreProductEntity> listStoreProduct = adminStoreProductRepository.findByProductGuid(UUID.fromString(productGuid));
        adminStoreProductRepository.deleteInBatch(listStoreProduct);

        adminProductRepository.delete(productEntity);
    }

    @Transactional
    public void updateProductPosition(AdminProductDTO payload) {
        adminProductRepository.updatePosition(payload.getGuid(), payload.getProductPosition());
    }

    @Transactional
    public void updateListProductPosition(List<AdminProductDTO> payload) {
        int pos = Constants.POSITION_INCREMENT - 1;
        for (AdminProductDTO productDTO : payload) {
            adminProductRepository.updatePosition(productDTO.getGuid(), pos);
            pos += Constants.POSITION_INCREMENT;
        }
    }

    public List<AdminProductDTO> getAllProductOrderByPositionAsc() {
        List<ProductEntity> listProduct = adminProductRepository.findListProductOrderByPositionAsc();
        return listProduct.stream().map(AdminProductDTO::new).collect(Collectors.toList());
    }
}
