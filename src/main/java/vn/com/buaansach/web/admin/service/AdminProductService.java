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
import vn.com.buaansach.web.admin.service.mapper.AdminProductMapper;
import vn.com.buaansach.web.common.service.FileService;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AdminProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final AdminProductMapper adminProductMapper;

    public AdminProductService(ProductRepository productRepository, CategoryRepository categoryRepository, FileService fileService, AdminProductMapper adminProductMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.adminProductMapper = adminProductMapper;
    }

    @Transactional
    public AdminProductDTO createProduct(AdminProductDTO adminProductDTO, MultipartFile image) {
        ProductEntity productEntity = adminProductMapper.dtoToEntity(adminProductDTO);
        if (productRepository.findOneByProductCode(productEntity.getProductCode()).isPresent()) {
            throw new BadRequestException("Product Code already in use");
        }
        CategoryEntity categoryEntity = categoryRepository.findOneByGuid(adminProductDTO.getCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + adminProductDTO.getCategoryGuid()));

        productEntity.setCategoryId(categoryEntity.getId());
        productEntity.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            productEntity.setProductImageUrl(fileEntity.getUrl());
        }

        return new AdminProductDTO(productRepository.save(productEntity), categoryEntity);
    }

    public AdminProductDTO getProduct(String productGuid) {
        return productRepository.findOneDtoByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productGuid));
    }

    @Transactional
    public AdminProductDTO updateProduct(AdminProductDTO adminProductDTO, MultipartFile image) {
        ProductEntity updateEntity = adminProductMapper.dtoToEntity(adminProductDTO);

        ProductEntity currentEntity = productRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + updateEntity.getGuid()));

        /* if change product code, check if code has been used or not */
        if (!updateEntity.getProductCode().equals(currentEntity.getProductCode())) {
            if (productRepository.findOneByProductCode(updateEntity.getProductCode()).isPresent()) {
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

        CategoryEntity categoryEntity = categoryRepository.findOneByGuid(adminProductDTO.getCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + adminProductDTO.getCategoryGuid()));
        updateEntity.setCategoryId(categoryEntity.getId());

        /* bind id to perform update */
        updateEntity.setId(currentEntity.getId());
        return new AdminProductDTO(productRepository.save(updateEntity), categoryEntity);
    }

    public Page<AdminProductDTO> getPageProduct(PageRequest request, String search) {
        return productRepository.findPageDtoWithKeyword(request, search.toLowerCase());
    }

    public void deleteProduct(String productGuid) {
        ProductEntity productEntity = productRepository.findOneByGuid(UUID.fromString(productGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productGuid));
        fileService.deleteByUrl(productEntity.getProductImageUrl());
        productRepository.delete(productEntity);
    }
}
