package vn.com.buaansach.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.ProductRepository;
import vn.com.buaansach.service.FileService;
import vn.com.buaansach.service.util.Constants;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class ProductManagementService {
    private final ProductRepository productRepository;
    private final FileService fileService;

    public ProductManagementService(ProductRepository productRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ProductEntity createProduct(ProductEntity productEntity, MultipartFile image) {
        if (productRepository.findOneByProductCode(productEntity.getProductCode()).isPresent()) {
            throw new BadRequestException("Mã sản phẩm đã được sử dụng");
        }
        productEntity.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.PRODUCT_IMAGE_PATH);
            productEntity.setProductImageUrl(fileEntity.getUrl());
        }
        return productRepository.save(productEntity);
    }

    @Transactional
    public ProductEntity updateProduct(ProductEntity updateEntity, MultipartFile image) {
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
