package vn.com.buaansach.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.StoreResourceException;
import vn.com.buaansach.repository.StoreRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

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
        if (storeRepository.findOneByCustomCode(entity.getCustomCode()).isPresent()) {
            throw new StoreResourceException("Custom code already in use!");
        }
        entity.setCode(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, "store_image");
            entity.setImageUrl(fileEntity.getUrl());
        }
        return storeRepository.save(entity);
    }

    @Transactional
    public StoreEntity update(StoreEntity entity, MultipartFile image) {
        Optional<StoreEntity> optional = storeRepository.findOneByCode(entity.getCode());
        if (optional.isPresent()) {
            StoreEntity currentEntity = optional.get();
            currentEntity.setName(entity.getName());
            currentEntity.setAddress(entity.getAddress());
            if (image != null) {
                fileService.deleteByUrl(currentEntity.getImageUrl());
                FileEntity newImage = fileService.uploadImage(image, "store_image");
                currentEntity.setImageUrl(newImage.getUrl());
            }
            return storeRepository.save(currentEntity);
        } else {
            throw new StoreResourceException("Store entity not found: " + entity);
        }
    }

    public Page<StoreEntity> getList(String keyword, PageRequest request) {
        return storeRepository.getPageStoreWithKeyword(request, keyword);
    }

    public Optional<StoreEntity> getOne(String storeCode) {
        return storeRepository.findOneByCode(UUID.fromString(storeCode));
    }

    public void delete(String storeCode) {
        storeRepository.deleteByCode(UUID.fromString(storeCode));
    }

}
