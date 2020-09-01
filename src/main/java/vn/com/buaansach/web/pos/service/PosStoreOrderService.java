package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.web.pos.repository.notification.PosStoreOrderNotificationRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreOrderDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreOrderStatusUpdateDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreOrderVisibilityUpdateDTO;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosStoreOrderService {
    private final Logger log = LoggerFactory.getLogger(PosStoreOrderService.class);
    private final PosStoreOrderNotificationRepository posStoreOrderNotificationRepository;
    private final PosStoreSecurity posStoreSecurity;

    public List<PosStoreOrderDTO> getListStoreOrder(String storeGuid, Instant startDate, Boolean hidden) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        List<StoreOrderNotificationEntity> list = new ArrayList<>();
//        if (hidden != null) {
//            list = posStoreOrderRepository
//                    .findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate, hidden);
//        } else {
//            list = posStoreOrderRepository
//                    .findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate);
//        }
        return list.stream().map(PosStoreOrderDTO::new).collect(Collectors.toList());
    }

    public StoreOrderNotificationEntity createStoreOrder(UUID storeGuid, UUID areaGuid, UUID seatGuid, UUID orderGuid, UUID orderProductGroup, int numberOfProduct) {
        StoreOrderNotificationEntity storeOrderNotificationEntity = new StoreOrderNotificationEntity();
//        storeOrderNotificationEntity.setGuid(UUID.randomUUID());
//        storeOrderNotificationEntity.setStoreOrderStatus(StoreOrderStatus.UNSEEN);
//        storeOrderNotificationEntity.setStoreOrderType(StoreOrderType.POS);
//        storeOrderNotificationEntity.setStoreOrderHidden(false);
//        storeOrderNotificationEntity.setStoreGuid(storeGuid);
//        storeOrderNotificationEntity.setAreaGuid(areaGuid);
//        storeOrderNotificationEntity.setSeatGuid(seatGuid);
//        storeOrderNotificationEntity.setOrderGuid(orderGuid);
        storeOrderNotificationEntity.setOrderProductGroup(orderProductGroup);
        storeOrderNotificationEntity.setNumberOfProduct(numberOfProduct);
        return posStoreOrderNotificationRepository.save(storeOrderNotificationEntity);
    }

    public PosStoreOrderDTO updateStoreOrder(PosStoreOrderStatusUpdateDTO payload, String currentUser) {
//        StoreOrderNotificationEntity entity = posStoreOrderRepository.findOneByGuid(payload.getGuid())
//                .orElseThrow(() -> new NotFoundException("pos@storeOrderNotFound@" + payload.getGuid()));
//        posStoreSecurity.blockAccessIfNotInStore(entity.getStoreGuid());
//        entity.setStoreOrderStatus(payload.getStoreOrderStatus());
//        if (entity.getFirstSeenBy() == null && payload.getStoreOrderStatus().equals(StoreOrderStatus.SEEN))
//            entity.setFirstSeenBy(currentUser);
//        return new PosStoreOrderDTO(posStoreOrderRepository.save(entity));
        return null;
    }

    @Transactional
    public void toggleVisibility(PosStoreOrderVisibilityUpdateDTO payload, String currentUser) {
//        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
//        List<StoreOrderNotificationEntity> list = posStoreOrderRepository.findByGuidIn(payload.getListGuid());

//        list.forEach(item -> {
//            if (!item.getStoreGuid().equals(payload.getStoreGuid()))
//                throw new BadRequestException("pos@storeOrderNotInStore@" + payload.getStoreGuid());
//        });
//
//        list = list.stream().peek(item -> {
//            item.setHideStoreOrder(payload.isHidden());
//            if (item.getFirstHideBy() == null && payload.isHidden()) item.setFirstHideBy(currentUser);
//        }).collect(Collectors.toList());

//        posStoreOrderRepository.saveAll(list);
    }

    /**
     * Old store orders should be automatically deleted after 7 days.
     * <p>
     * This is scheduled to get fired at 1:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeOldStoreOrder() {
//        Instant deletePoint = Instant.now().minus(7, ChronoUnit.DAYS);
//        List<StoreOrderNotificationEntity> list = posStoreOrderRepository
//                .findByCreatedDateBefore(deletePoint);
//        log.debug("[Scheduling] Deleting old store order data [{}]", list);
//        posStoreOrderRepository.deleteAll(list);
    }
}
