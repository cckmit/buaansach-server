package vn.com.buaansach.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final UserRepository userRepository;
    private final AreaService areaService;

    public StoreService(StoreRepository storeRepository, FileService fileService, UserRepository userRepository, AreaService areaService) {
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.areaService = areaService;
    }

    @Transactional
    public StoreEntity createStore(StoreEntity entity, MultipartFile image) {
        if (storeRepository.findOneByStoreCode(entity.getStoreCode()).isPresent()) {
            throw new BadRequestException("error.create.codeExists");
        }
        entity.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, "store_image");
            entity.setStoreImageUrl(fileEntity.getUrl());
        }
        return storeRepository.save(entity);
    }

    @Transactional
    public StoreEntity updateStore(StoreEntity updateEntity, MultipartFile image) {
        Optional<StoreEntity> optional = storeRepository.findOneByGuid(updateEntity.getGuid());
        if (!optional.isPresent()) throw new ResourceNotFoundException("Store not found with guid: " + updateEntity.getGuid());

        StoreEntity currentEntity = optional.get();

        /* check if store code has changed or not */
        if (!updateEntity.getStoreCode().equals(currentEntity.getStoreCode())) {

            /* if changed then check whether the new code has been used or not */
            storeRepository.findOneByStoreCode(updateEntity.getStoreCode()).ifPresent(anotherEntity -> {
                /* another entity has taken the new code */
                throw new BadRequestException("Code already in use!");
            });

            /* if store code has not been taken, just update */
            currentEntity.setStoreCode(updateEntity.getStoreCode());
        }

        if (image != null) {
            /* delete current entity's image first */
            fileService.deleteByUrl(currentEntity.getStoreImageUrl());
            /* then set the new image url */
            FileEntity newImage = fileService.uploadImage(image, "store_image");
            currentEntity.setStoreImageUrl(newImage.getUrl());
        } else {
            /* re-set entity's image url in case image url is cleared */
            currentEntity.setStoreImageUrl(updateEntity.getStoreImageUrl());
        }

        /* just update attributes */
        currentEntity.setStoreName(updateEntity.getStoreName());
        currentEntity.setStoreAddress(updateEntity.getStoreAddress());
        currentEntity.setStoreStatus(updateEntity.getStoreStatus());
        currentEntity.setStoreOwnerName(updateEntity.getStoreOwnerName());
        currentEntity.setStoreOwnerPhone(updateEntity.getStoreOwnerPhone());
        currentEntity.setStoreOwnerEmail(updateEntity.getStoreOwnerEmail());
        currentEntity.setStoreTaxCode(updateEntity.getStoreTaxCode());
        currentEntity.setLastUpdateReason(updateEntity.getLastUpdateReason());

        return storeRepository.save(currentEntity);
    }

    public Page<StoreEntity> getPageStore(String search, PageRequest request) {
        return storeRepository.findPageStoreWithKeyword(request, search.toLowerCase());
    }

    public StoreEntity getOneByGuid(String storeGuid) {
        Optional<StoreEntity> optional = storeRepository.findOneByGuid(UUID.fromString(storeGuid));
        if (!optional.isPresent()) throw new ResourceNotFoundException("Store not found with guid: " + storeGuid);
        return optional.get();
    }

    @Transactional
    public void deleteStore(String storeGuid) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        fileService.deleteByUrl(storeEntity.getStoreImageUrl());
        areaService.deleteAreaByStoreId(storeEntity.getId());
        storeRepository.delete(storeEntity);
    }

}
