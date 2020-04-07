package vn.com.buaansach.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.model.entity.FileEntity;
import vn.com.buaansach.model.entity.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.service.AreaService;
import vn.com.buaansach.service.FileService;
import vn.com.buaansach.util.Constants;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AdminStoreService {
    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final AreaService areaService;

    public AdminStoreService(StoreRepository storeRepository, FileService fileService, AreaService areaService) {
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.areaService = areaService;
    }

    @Transactional
    public StoreEntity createStore(StoreEntity payload, MultipartFile image) {
        if (storeRepository.findOneByStoreCode(payload.getStoreCode()).isPresent()) {
            throw new BadRequestException("Mã cửa hàng đã được sử dụng");
        }
        payload.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.STORE_IMAGE_PATH);
            payload.setStoreImageUrl(fileEntity.getUrl());
        }
        return storeRepository.save(payload);
    }

    @Transactional
    public StoreEntity updateStore(StoreEntity updateEntity, MultipartFile image) {
        StoreEntity currentEntity = storeRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("store", "guid", updateEntity.getGuid()));

        /* if change store code, check if code has been used or not */
        if (!updateEntity.getStoreCode().equals(currentEntity.getStoreCode())) {
            storeRepository.findOneByStoreCode(updateEntity.getStoreCode()).ifPresent(anotherEntity -> {
                throw new BadRequestException("Mã cửa hàng đã được sử dụng");
            });
        }

        if (image != null) {
            /* delete current entity's image first */
            fileService.deleteByUrl(currentEntity.getStoreImageUrl());
            FileEntity newImage = fileService.uploadImage(image, Constants.STORE_IMAGE_PATH);
            updateEntity.setStoreImageUrl(newImage.getUrl());
        } else {
            if (updateEntity.getStoreImageUrl() == null || updateEntity.getStoreImageUrl().isEmpty()) {
                fileService.deleteByUrl(currentEntity.getStoreImageUrl());
            }
        }

        updateEntity.setId(currentEntity.getId());
        return storeRepository.save(updateEntity);
    }

    @Transactional
    public void deleteStore(String storeGuid) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("store", "guid", storeGuid));
        fileService.deleteByUrl(storeEntity.getStoreImageUrl());
        areaService.deleteAreaByStoreId(storeEntity.getId());
        storeRepository.delete(storeEntity);
    }
}
