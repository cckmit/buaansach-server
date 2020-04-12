package vn.com.buaansach.web.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.CategoryEntity;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.CategoryRepository;
import vn.com.buaansach.repository.ProductRepository;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.service.dto.AdminProductDTO;
import vn.com.buaansach.web.common.service.FileService;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AdminProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public AdminProductService(ProductRepository productRepository, CategoryRepository categoryRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ProductEntity createProduct(AdminProductDTO adminProductDTO, MultipartFile image) {
        ProductEntity productEntity = adminProductDTO.toEntity();
        if (productRepository.findOneByProductCode(productEntity.getProductCode()).isPresent()) {
            throw new BadRequestException("Mã sản phẩm đã được sử dụng");
        }
        CategoryEntity categoryEntity = categoryRepository.findOneByGuid(adminProductDTO.getCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + adminProductDTO.getCategoryGuid()));
        productEntity.setCategoryId(categoryEntity.getId());
        productEntity.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            productEntity.setProductImageUrl(fileEntity.getUrl());
        }
        return productRepository.save(productEntity);
    }

    public ProductEntity getProduct(String productGuid) {
        return productRepository.findOneByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm: " + productGuid));
    }

    @Transactional
    public ProductEntity updateProduct(AdminProductDTO adminProductDTO, MultipartFile image) {
        ProductEntity updateEntity = adminProductDTO.toEntity();
        ProductEntity currentEntity = productRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + updateEntity.getGuid()));

        /* if change product code, check if code has been used or not */
        if (!updateEntity.getProductCode().equals(currentEntity.getProductCode())) {
            if (productRepository.findOneByProductCode(updateEntity.getProductCode()).isPresent()) {
                throw new BadRequestException("Mã sản phẩm đã được sử dụng");
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

        CategoryEntity categoryEntity = categoryRepository.findOneByGuid(adminProductDTO.getCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + adminProductDTO.getCategoryGuid()));
        updateEntity.setCategoryId(categoryEntity.getId());

        /* bind id to perform update */
        updateEntity.setId(currentEntity.getId());
        return productRepository.save(updateEntity);
    }

    public Page<ProductEntity> getPageProduct(PageRequest request, String search) {
        return productRepository.findPageProductWithKeyword(request, search.toLowerCase());
    }

    public void deleteProduct(String productGuid) {
        ProductEntity productEntity = productRepository.findOneByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + productGuid));
        fileService.deleteByUrl(productEntity.getProductImageUrl());
        productRepository.delete(productEntity);
    }
}
