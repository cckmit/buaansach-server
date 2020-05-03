package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.AdminCategoryRepository;
import vn.com.buaansach.web.admin.repository.AdminProductCategoryRepository;
import vn.com.buaansach.web.admin.repository.AdminProductRepository;
import vn.com.buaansach.web.user.service.FileService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class AdminCategoryService {
    private final AdminCategoryRepository adminCategoryRepository;
    private final FileService fileService;
    private final AdminProductRepository adminProductRepository;
    private final AdminProductCategoryRepository adminProductCategoryRepository;

    public AdminCategoryService(AdminCategoryRepository adminCategoryRepository, FileService fileService, AdminProductRepository adminProductRepository, AdminProductCategoryRepository adminProductCategoryRepository) {
        this.adminCategoryRepository = adminCategoryRepository;
        this.fileService = fileService;
        this.adminProductRepository = adminProductRepository;
        this.adminProductCategoryRepository = adminProductCategoryRepository;
    }

    @Transactional
    public CategoryEntity createCategory(CategoryEntity categoryEntity, MultipartFile image) {
        if (adminCategoryRepository.findOneByCategoryName(categoryEntity.getCategoryName()).isPresent()) {
            throw new BadRequestException("Category Name already in use");
        }
        Integer lastPos = adminCategoryRepository.findLastCategoryPosition();
        int pos = lastPos != null ? lastPos + Constants.POSITION_INCREMENT : Constants.POSITION_INCREMENT - 1;
        categoryEntity.setGuid(UUID.randomUUID());
        categoryEntity.setCategoryPosition(pos);
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.CATEGORY_IMAGE_PATH);
            categoryEntity.setCategoryImageUrl(fileEntity.getUrl());
        }
        return adminCategoryRepository.save(categoryEntity);
    }

    public CategoryEntity getCategory(String categoryGuid) {
        return adminCategoryRepository.findOneByGuid(UUID.fromString(categoryGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with guid: " + categoryGuid));
    }

    @Transactional
    public CategoryEntity updateCategory(CategoryEntity updateEntity, MultipartFile image) {
        CategoryEntity currentEntity = adminCategoryRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with guid: " + updateEntity.getGuid()));

        /* if change category name, check if name has been used or not */
        if (!updateEntity.getCategoryName().equals(currentEntity.getCategoryName())) {
            if (adminCategoryRepository.findOneByCategoryName(updateEntity.getCategoryName()).isPresent()) {
                throw new BadRequestException("Category Name already in use");
            }
        }

        if (image != null) {
            /* if update image */
            fileService.deleteByUrl(currentEntity.getCategoryImageUrl());
            FileEntity newImage = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            updateEntity.setCategoryImageUrl(newImage.getUrl());
        } else {
            /* if updateEntity has imageUrl = null or empty
             * => user has cleared image
             * => delete currentEntity's image
             */
            if (updateEntity.getCategoryImageUrl() == null || updateEntity.getCategoryImageUrl().isEmpty()) {
                fileService.deleteByUrl(currentEntity.getCategoryImageUrl());
            }
        }

        /* bind id to perform update */
        updateEntity.setId(currentEntity.getId());
        return adminCategoryRepository.save(updateEntity);
    }

    public List<CategoryEntity> getAllCategory() {
        return adminCategoryRepository.findListCategory();
    }

    @Transactional
    public void deleteCategory(String categoryGuid) {
        CategoryEntity categoryEntity = adminCategoryRepository.findOneByGuid(UUID.fromString(categoryGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with guid: " + categoryGuid));
        fileService.deleteByUrl(categoryEntity.getCategoryImageUrl());
        adminProductCategoryRepository.deleteByCategoryGuid(categoryEntity.getGuid());
        adminCategoryRepository.delete(categoryEntity);
    }
}
