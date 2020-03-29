package vn.com.buaansach.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.StoreResourceException;
import vn.com.buaansach.repository.StoreRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final FileService fileService;

    public StoreService(StoreRepository storeRepository, FileService fileService) {
        this.storeRepository = storeRepository;
        this.fileService = fileService;
    }

    @Transactional
    public StoreEntity create(StoreEntity entity, MultipartFile image) {
        if (entity.getId() != null) throw new BadRequestException("A new entity cannot already have and ID");
        if (storeRepository.findOneByCode(entity.getCode()).isPresent()) {
            throw new BadRequestException("Code already in use!");
        }
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, "store_image");
            entity.setImageUrl(fileEntity.getUrl());
        }
        return storeRepository.save(entity);
    }

    @Transactional
    public StoreEntity update(StoreEntity updateEntity, MultipartFile image) {
        if (updateEntity.getId() == null) throw new BadRequestException("Entity must have an ID to perform update");
        Optional<StoreEntity> optional = storeRepository.findOneById(updateEntity.getId());
        if (!optional.isPresent()) throw new StoreResourceException("Entity not found");
        /* else */
        StoreEntity currentEntity = optional.get();

        /* check if store code has changed or not */
        if (!updateEntity.getCode().equals(currentEntity.getCode())) {
            /* if changed then check whether the new code has been used or not */
            storeRepository.findOneByCode(updateEntity.getCode()).ifPresent(anotherEntity -> {
                /* another entity has taken the new code */
                throw new BadRequestException("Code already in use!");
            });
        }

        if (image != null) {
            /* delete current entity's image first */
            fileService.deleteByUrl(currentEntity.getImageUrl());
            /* then set the new image url */
            FileEntity newImage = fileService.uploadImage(image, "store_image");
            currentEntity.setImageUrl(newImage.getUrl());
        } else {
            /* re-set entity's image url in case image url is cleared */
            currentEntity.setImageUrl(updateEntity.getImageUrl());
        }

        /* just update attributes */
        currentEntity.setName(updateEntity.getName());
        currentEntity.setAddress(updateEntity.getAddress());
        currentEntity.setActivated(updateEntity.isActivated());
        currentEntity.setOwnerName(updateEntity.getOwnerName());
        currentEntity.setOwnerPhone(updateEntity.getOwnerPhone());
        currentEntity.setOwnerEmail(updateEntity.getOwnerEmail());
        currentEntity.setTaxCode(updateEntity.getTaxCode());
        currentEntity.setUpdateReason(updateEntity.getUpdateReason());

        return storeRepository.save(currentEntity);
    }

    public Page<StoreEntity> getList(String search, PageRequest request) {
        return storeRepository.getPageStoreWithKeyword(request, search.toLowerCase());
    }

    public Optional<StoreEntity> getOne(String storeCode) {
        return storeRepository.findOneByCode(storeCode);
    }

    public void toggleStatus(String storeCode) {
        storeRepository.findOneByCode(storeCode).ifPresent(storeEntity -> {
            storeEntity.setActivated(!storeEntity.isActivated());
            /*
            * there will be some codes to clear or disable entity related to this store.
            * */
            storeRepository.save(storeEntity);
        });
    }

    public void delete(String storeCode) {
        storeRepository.findOneByCode(storeCode).ifPresent(storeEntity -> {
            fileService.deleteByUrl(storeEntity.getImageUrl());
            /*
             * there will be some codes to clear or disable entity related to this store.
             * */
            storeRepository.delete(storeEntity);
        });
    }

}
