package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.entity.common.SequenceEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.common.AdminSequenceRepository;
import vn.com.buaansach.web.admin.repository.order.AdminOrderProductRepository;
import vn.com.buaansach.web.admin.repository.order.AdminOrderRepository;
import vn.com.buaansach.web.admin.repository.store.*;
import vn.com.buaansach.web.shared.service.CodeService;
import vn.com.buaansach.web.shared.service.FileService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminStoreService {
    private final FileService fileService;
    private final AdminStoreRepository adminStoreRepository;
    private final CodeService codeService;
    private final AdminSequenceRepository adminSequenceRepository;
    private final AdminStoreNotificationService adminStoreNotificationService;
    private final AdminAreaService adminAreaService;
    private final AdminSeatService adminSeatService;
    private final AdminStoreUserService adminStoreUserService;
    private final AdminStoreProductService adminStoreProductService;
    private final AdminStoreSaleService adminStoreSaleService;

    @Transactional
    public StoreEntity createStore(StoreEntity payload, MultipartFile image) {
        payload.setGuid(UUID.randomUUID());
        payload.setStoreCode(codeService.generateCodeForStore());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.STORE_IMAGE_PATH);
            payload.setStoreImageUrl(fileEntity.getUrl());
        }
        SequenceEntity sequenceEntity = new SequenceEntity(payload.getStoreCode(), 0);
        adminSequenceRepository.save(sequenceEntity);
        return adminStoreRepository.save(payload);
    }

    @Transactional
    public StoreEntity updateStore(StoreEntity updateEntity, MultipartFile image) {
        StoreEntity currentEntity = adminStoreRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        /* do not allow change store code */
        updateEntity.setStoreCode(currentEntity.getStoreCode());

        if (image != null) {
            /* delete current entity's image before update new image */
            fileService.deleteByUrl(currentEntity.getStoreImageUrl());
            FileEntity newImage = fileService.uploadImage(image, Constants.STORE_IMAGE_PATH);
            updateEntity.setStoreImageUrl(newImage.getUrl());
        } else {
            if (updateEntity.getStoreImageUrl() == null || updateEntity.getStoreImageUrl().isEmpty()) {
                fileService.deleteByUrl(currentEntity.getStoreImageUrl());
            }
        }
        /* set id to perform update */
        updateEntity.setId(currentEntity.getId());
        return adminStoreRepository.save(updateEntity);
    }

    public Page<StoreEntity> getPageStore(PageRequest request, String search) {
        return adminStoreRepository.findPageStoreWithKeyword(request, search.toLowerCase());
    }

    public StoreEntity getOneStore(String storeGuid) {
        return adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }

    @Transactional
    public void deleteStore(UUID storeGuid) {
        /* be careful when delete store - must test more cases to catch all possible errors */
        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(storeGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        adminSeatService.deleteByStore(storeGuid);
        adminAreaService.deleteByStore(storeGuid);
        adminStoreUserService.deleteByStore(storeGuid);
        adminStoreProductService.deleteByStore(storeGuid);
        adminStoreSaleService.deleteByStore(storeGuid);
        adminStoreNotificationService.deleteNotificationByStore(storeGuid);

        fileService.deleteByUrl(storeEntity.getStoreImageUrl());
        adminStoreRepository.delete(storeEntity);
    }

    public List<StoreEntity> getAllStore() {
        return adminStoreRepository.findAll();
    }
}
