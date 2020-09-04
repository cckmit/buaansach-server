package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.order.AdminOrderProductRepository;
import vn.com.buaansach.web.admin.repository.order.AdminOrderRepository;
import vn.com.buaansach.web.admin.repository.store.*;
import vn.com.buaansach.web.shared.service.FileService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStoreService {
    private final FileService fileService;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminAreaRepository adminAreaRepository;
    private final AdminStoreUserRepository adminStoreUserRepository;
    private final AdminSeatRepository adminSeatRepository;
    private final AdminCodeService adminCodeService;
    private final AdminStoreProductRepository adminStoreProductRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderProductRepository adminOrderProductRepository;

    @Transactional
    public StoreEntity createStore(StoreEntity payload, MultipartFile image) {
        payload.setGuid(UUID.randomUUID());
        payload.setStoreCode(adminCodeService.generateCodeForStore());
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.STORE_IMAGE_PATH);
            payload.setStoreImageUrl(fileEntity.getUrl());
        }
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
    public void deleteStore(String storeGuid) {
        /* be careful when delete store - must test more cases to catch all possible errors */
        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        List<SeatEntity> listSeat = adminSeatRepository.findListSeatByStoreGuid(storeEntity.getGuid());
        List<UUID> listSeatGuid = listSeat.stream().map(SeatEntity::getGuid).collect(Collectors.toList());
        List<OrderEntity> listOrder = adminOrderRepository.findBySeatGuidIn(listSeatGuid);
        List<UUID> listOrderGuid = listOrder.stream().map(OrderEntity::getGuid).collect(Collectors.toList());
        List<OrderProductEntity> listOrderProduct = adminOrderProductRepository.findByOrderGuidIn(listOrderGuid);


        /* delete all things related to store */
        adminOrderProductRepository.deleteInBatch(listOrderProduct);
        adminOrderRepository.deleteInBatch(listOrder);

        adminSeatRepository.deleteInBatch(listSeat);
        adminAreaRepository.deleteByStoreGuid(storeEntity.getGuid());
        adminStoreUserRepository.deleteByStoreGuid(storeEntity.getGuid());
        adminStoreProductRepository.deleteByStoreGuid(storeEntity.getGuid());
        fileService.deleteByUrl(storeEntity.getStoreImageUrl());
        adminStoreRepository.delete(storeEntity);
    }

    public List<StoreEntity> getAllStore() {
        return adminStoreRepository.findAll();
    }
}
