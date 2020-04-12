package vn.com.buaansach.web.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.CategoryEntity;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.CategoryRepository;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.common.service.FileService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public AdminCategoryService(CategoryRepository categoryRepository, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    @Transactional
    public CategoryEntity createCategory(CategoryEntity categoryEntity, MultipartFile image) {
        if (categoryRepository.findOneByCategoryName(categoryEntity.getCategoryName()).isPresent()) {
            throw new BadRequestException("Tên danh mục đã được sử dụng");
        }
        categoryEntity.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.CATEGORY_IMAGE_PATH);
            categoryEntity.setCategoryImageUrl(fileEntity.getUrl());
        }
        return categoryRepository.save(categoryEntity);
    }

    public CategoryEntity getCategory(String categoryGuid) {
        return categoryRepository.findOneByGuid(UUID.fromString(categoryGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + categoryGuid));
    }

    @Transactional
    public CategoryEntity updateCategory(CategoryEntity updateEntity, MultipartFile image) {
        CategoryEntity currentEntity = categoryRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + updateEntity.getGuid()));

        /* if change category name, check if name has been used or not */
        if (!updateEntity.getCategoryName().equals(currentEntity.getCategoryName())) {
            if (categoryRepository.findOneByCategoryName(updateEntity.getCategoryName()).isPresent()) {
                throw new BadRequestException("Tên danh mục đã được sử dụng");
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
        CategoryEntity result = categoryRepository.save(updateEntity);
        return result;
    }

    public List<CategoryEntity> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Page<CategoryEntity> getPageCategory(PageRequest request, String search) {
        return categoryRepository.findPageCategoryWithKeyword(request, search.toLowerCase());
    }

    public void deleteCategory(String categoryGuid) {
        CategoryEntity categoryEntity = categoryRepository.findOneByGuid(UUID.fromString(categoryGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + categoryGuid));
        fileService.deleteByUrl(categoryEntity.getCategoryImageUrl());
        categoryRepository.delete(categoryEntity);
    }
}
