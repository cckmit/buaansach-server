package vn.com.buaansach.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.StoreResourceException;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.security.util.SecurityUtils;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final UserRepository userRepository;

    public StoreService(StoreRepository storeRepository, FileService fileService, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    @Transactional
    public StoreEntity create(StoreEntity entity, MultipartFile image) {
        if (storeRepository.findOneByStoreCode(entity.getStoreCode()).isPresent()) {
            throw new BadRequestException("error.create.codeExists");
        }
        entity.setGuid(UUID.randomUUID());
        /* set the creator is store owner first, change it later with api: api/store/switch-owner */
        Optional<UserEntity> optional = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (!optional.isPresent()) throw new BadRequestException("error.create.userNotFound");
        optional.ifPresent(entity::setStoreOwnerUser);

        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, "store_image");
            entity.setStoreImageUrl(fileEntity.getUrl());
        }

        return storeRepository.save(entity);
    }

    @Transactional
    public StoreEntity update(StoreEntity updateEntity, MultipartFile image) {
        Optional<StoreEntity> optional = storeRepository.findOneByGuid(updateEntity.getGuid());
        if (!optional.isPresent()) throw new StoreResourceException("error.resource.notFound");

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

    public Page<StoreEntity> getList(String search, PageRequest request) {
        return storeRepository.findPageStoreWithKeyword(request, search.toLowerCase());
    }

    public Optional<StoreEntity> getOneByGuid(String storeGuid) {
        return storeRepository.findOneByGuid(UUID.fromString(storeGuid));
    }

    public void delete(String storeCode) {
        storeRepository.findOneByStoreCode(storeCode).ifPresent(storeEntity -> {
            fileService.deleteByUrl(storeEntity.getStoreImageUrl());
            /*
             * there will be some codes to clear or disable entity related to this store.
             * */
            storeRepository.delete(storeEntity);
        });
    }

}
