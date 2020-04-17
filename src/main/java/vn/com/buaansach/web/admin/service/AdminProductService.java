package vn.com.buaansach.web.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.AdminCategoryRepository;
import vn.com.buaansach.web.admin.repository.AdminProductRepository;
import vn.com.buaansach.web.user.service.FileService;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AdminProductService {
    private final AdminProductRepository adminProductRepository;
    private final AdminCategoryRepository adminCategoryRepository;
    private final FileService fileService;

    public AdminProductService(AdminProductRepository adminProductRepository, AdminCategoryRepository adminCategoryRepository, FileService fileService) {
        this.adminProductRepository = adminProductRepository;
        this.adminCategoryRepository = adminCategoryRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ProductEntity createProduct(ProductEntity productEntity, MultipartFile image) {
        if (adminProductRepository.findOneByProductCode(productEntity.getProductCode()).isPresent()) {
            throw new BadRequestException("Product Code already in use");
        }
        adminCategoryRepository.findOneByGuid(productEntity.getCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with guid: " + productEntity.getCategoryGuid()));

        productEntity.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            productEntity.setProductImageUrl(fileEntity.getUrl());
        }
        return adminProductRepository.save(productEntity);
    }

    public ProductEntity getProduct(String productGuid) {
        return adminProductRepository.findOneByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with guid: " + productGuid));
    }

    @Transactional
    public ProductEntity updateProduct(ProductEntity updateEntity, MultipartFile image) {
        ProductEntity currentEntity = adminProductRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with guid: " + updateEntity.getGuid()));

        adminCategoryRepository.findOneByGuid(updateEntity.getCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with guid: " + updateEntity.getCategoryGuid()));

        String updateProductCode = updateEntity.getProductCode().toLowerCase();
        String currentProductCode = currentEntity.getProductCode().toLowerCase();
        /* Change product code, check if code has been used or not */
        if (!updateProductCode.equals(currentProductCode)) {
            if (adminProductRepository.findOneByProductCode(updateEntity.getProductCode()).isPresent()) {
                throw new BadRequestException("Product Code already in use");
            }
        }

        if (image != null) {
            /* if update image */
            fileService.deleteByUrl(currentEntity.getProductImageUrl());
            FileEntity newImage = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            updateEntity.setProductImageUrl(newImage.getUrl());
        } else {
            /* if updateEntity has imageUrl = null or empty
             * => user has cleared image
             * => delete currentEntity's image
             */
            if (updateEntity.getProductImageUrl() == null || updateEntity.getProductImageUrl().isEmpty()) {
                fileService.deleteByUrl(currentEntity.getProductImageUrl());
            }
        }

        /* set id to perform update */
        updateEntity.setId(currentEntity.getId());
        return adminProductRepository.save(updateEntity);
    }

    public Page<ProductEntity> getPageProduct(PageRequest request, String search) {
        return adminProductRepository.findPageProductWithKeyword(request, search.toLowerCase());
    }

    public void deleteProduct(String productGuid) {
        ProductEntity productEntity = adminProductRepository.findOneByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with guid: " + productGuid));
        fileService.deleteByUrl(productEntity.getProductImageUrl());
        fileService.deleteByUrl(productEntity.getProductThumbnailUrl());
        adminProductRepository.delete(productEntity);
    }
}
