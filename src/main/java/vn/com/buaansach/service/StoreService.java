package vn.com.buaansach.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.exception.StoreResourceException;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.dto.StoreDTO;
import vn.com.buaansach.service.dto.StoreOwnerChangeDTO;

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
    public StoreDTO create(StoreEntity entity, MultipartFile image) {
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

        return new StoreDTO(storeRepository.save(entity));
    }

    @Transactional
    public StoreDTO update(StoreEntity updateEntity, MultipartFile image) {
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

        return new StoreDTO(storeRepository.save(currentEntity));
    }

    public Page<StoreDTO> getList(String search, PageRequest request) {
        return storeRepository.findPageStoreWithKeyword(request, search.toLowerCase()).map(StoreDTO::new);
    }

    public StoreDTO getOneByGuid(String storeGuid) {
        Optional<StoreEntity> optional = storeRepository.findOneByGuid(UUID.fromString(storeGuid));
        if (!optional.isPresent()) throw new StoreResourceException("error.resource.notFound");
        return new StoreDTO(optional.get());
    }

    public void delete(String storeGuid) {
        Optional<StoreEntity> optional = storeRepository.findOneByGuid(UUID.fromString(storeGuid));
        if (!optional.isPresent()) throw new ResourceNotFoundException("Store", "guid", storeGuid);
        fileService.deleteByUrl(optional.get().getStoreImageUrl());
        storeRepository.delete(optional.get());
    }

    public StoreDTO changeStoreOwner(StoreOwnerChangeDTO dto) {
        Optional<UserEntity> userOptional = userRepository.findOneByLoginOrEmail(dto.getUsernameOrEmail().toLowerCase(),
                dto.getUsernameOrEmail().toLowerCase());
        if (!userOptional.isPresent())
            throw new ResourceNotFoundException("User", "usernameOrEmail", dto.getUsernameOrEmail());
        Optional<StoreEntity> storeOptional = storeRepository.findOneByGuid(UUID.fromString(dto.getStoreGuid()));
        if (!storeOptional.isPresent())
            throw new ResourceNotFoundException("store", "guid", dto.getStoreGuid());
        StoreEntity entity = storeOptional.get();
        entity.setStoreOwnerUser(userOptional.get());
        return new StoreDTO(storeRepository.save(entity));
    }
}
