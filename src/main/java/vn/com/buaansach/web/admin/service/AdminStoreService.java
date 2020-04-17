package vn.com.buaansach.web.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.AdminAreaRepository;
import vn.com.buaansach.web.admin.repository.AdminSeatRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreUserRepository;
import vn.com.buaansach.web.user.service.FileService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class AdminStoreService {
    private final FileService fileService;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminAreaRepository adminAreaRepository;
    private final AdminStoreUserRepository adminStoreUserRepository;
    private final AdminSeatRepository adminSeatRepository;

    public AdminStoreService(AdminStoreRepository adminStoreRepository, FileService fileService, AdminAreaRepository adminAreaRepository, AdminStoreUserRepository adminStoreUserRepository, AdminSeatRepository adminSeatRepository) {
        this.adminStoreRepository = adminStoreRepository;
        this.fileService = fileService;
        this.adminStoreUserRepository = adminStoreUserRepository;
        this.adminAreaRepository = adminAreaRepository;
        this.adminSeatRepository = adminSeatRepository;
    }

    @Transactional
    public StoreEntity createStore(StoreEntity payload, MultipartFile image) {
        if (adminStoreRepository.findOneByStoreCode(payload.getStoreCode()).isPresent()) {
            throw new BadRequestException("Store Code already in use");
        }
        payload.setGuid(UUID.randomUUID());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.STORE_IMAGE_PATH);
            payload.setStoreImageUrl(fileEntity.getUrl());
        }
        return adminStoreRepository.save(payload);
    }

    @Transactional
    public StoreEntity updateStore(StoreEntity updateEntity, MultipartFile image) {
        StoreEntity currentEntity = adminStoreRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + updateEntity.getGuid()));

        String updateStoreCode = updateEntity.getStoreCode().toLowerCase();
        String currentStoreCode = currentEntity.getStoreCode().toLowerCase();
        /* Change store code, check if code has been used or not */
        if (!updateStoreCode.equals(currentStoreCode)) {
            adminStoreRepository.findOneByStoreCode(updateEntity.getStoreCode()).ifPresent(anotherEntity -> {
                throw new BadRequestException("Store Code already in use");
            });
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
    }

    @Transactional
    public void deleteStore(String storeGuid) {
        /* be careful when delete store - must test more cases to catch all possible errors */
        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid:" + storeGuid));
        List<SeatEntity> listSeat = adminSeatRepository.findListSeatByStoreGuid(storeEntity.getGuid());
        adminSeatRepository.deleteInBatch(listSeat);
        adminAreaRepository.deleteByStoreGuid(storeEntity.getGuid());
        adminStoreUserRepository.deleteByStoreGuid(storeEntity.getGuid());
        fileService.deleteByUrl(storeEntity.getStoreImageUrl());
        adminStoreRepository.delete(storeEntity);
    }

}
