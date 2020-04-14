package vn.com.buaansach.web.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.web.common.service.FileService;
import vn.com.buaansach.util.Constants;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AdminStoreService {
    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final AdminAreaService adminAreaService;
    private final AdminStoreUserService adminStoreUserService;

    public AdminStoreService(StoreRepository storeRepository, FileService fileService, AdminAreaService adminAreaService, AdminStoreUserService adminStoreUserService) {
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.adminAreaService = adminAreaService;
        this.adminStoreUserService = adminStoreUserService;
    }

    @Transactional
    public StoreEntity createStore(StoreEntity payload, MultipartFile image) {
        if (storeRepository.findOneByStoreCode(payload.getStoreCode()).isPresent()) {
            throw new BadRequestException("Store Code already in use");
        }
        payload.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.STORE_IMAGE_PATH);
            payload.setStoreImageUrl(fileEntity.getUrl());
        }
        return storeRepository.save(payload);
    }

    public Page<StoreEntity> getPageStore(String search, PageRequest request) {
        return storeRepository.findPageStoreWithKeyword(request, search.toLowerCase());
    }

    public StoreEntity getOneStore(String storeGuid) {
        return storeRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeGuid));
    }

    @Transactional
    public StoreEntity updateStore(StoreEntity updateEntity, MultipartFile image) {
        StoreEntity currentEntity = storeRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("store", "guid", updateEntity.getGuid()));

        /* if change store code, check if code has been used or not */
        if (!updateEntity.getStoreCode().toLowerCase().equals(currentEntity.getStoreCode().toLowerCase())) {
            storeRepository.findOneByStoreCode(updateEntity.getStoreCode()).ifPresent(anotherEntity -> {
                throw new BadRequestException("Store Code alreay in use");
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
        adminAreaService.deleteAllAreaByStoreId(storeEntity.getId());
        fileService.deleteByUrl(storeEntity.getStoreImageUrl());
        adminStoreUserService.deleteByStoreGuid(storeEntity.getGuid());
        storeRepository.delete(storeEntity);
    }

}
