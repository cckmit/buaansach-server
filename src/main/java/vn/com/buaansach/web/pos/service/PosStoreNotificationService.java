package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.enumeration.StoreOrderType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.pos.repository.notification.PosStoreNotificationRepository;
import vn.com.buaansach.web.pos.repository.notification.PosStoreOrderNotificationRepository;
import vn.com.buaansach.web.pos.repository.notification.PosStorePayRequestNotificationRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreNotificationDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreNotificationVisibilityUpdateDTO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosStoreNotificationService {
    private final Logger log = LoggerFactory.getLogger(PosStoreNotificationService.class);
    private final PosStoreSecurity posStoreSecurity;
    private final PosStorePayRequestNotificationRepository posStorePayRequestNotificationRepository;
    private final PosStoreOrderNotificationRepository posStoreOrderNotificationRepository;
    private final PosStoreNotificationRepository posStoreNotificationRepository;

    /**
     * Old store notification should be automatically deleted after 10 days.
     * <p>
     * This is scheduled to get fired at 2:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeOldStoreNotification() {
        Instant deletePoint = Instant.now().minus(10, ChronoUnit.DAYS);
        List<StoreNotificationEntity> list = posStoreNotificationRepository.findByCreatedDateBefore(deletePoint);
        List<UUID> listGuid = list.stream().map(StoreNotificationEntity::getGuid).collect(Collectors.toList());
        log.debug("[Scheduling] Deleting old store notification data [{}]", list);
        posStorePayRequestNotificationRepository.deleteByStoreNotificationGuidIn(listGuid);
        posStoreOrderNotificationRepository.deleteByStoreNotificationGuidIn(listGuid);
        posStoreNotificationRepository.deleteAll(list);
    }

    public PosStoreNotificationDTO createStoreOrderNotification(UUID storeGuid, UUID areaGuid, UUID seatGuid, UUID orderGuid, UUID orderProductGroup, int numberOfProduct) {
        StoreNotificationEntity notificationEntity = new StoreNotificationEntity();
        UUID notificationGuid = UUID.randomUUID();
        notificationEntity.setGuid(notificationGuid);
        notificationEntity.setStoreNotificationStatus(StoreNotificationStatus.UNSEEN);
        notificationEntity.setStoreNotificationType(StoreNotificationType.ORDER_UPDATE);
        notificationEntity.setStoreNotificationHidden(false);
        notificationEntity.setStoreGuid(storeGuid);
        notificationEntity.setAreaGuid(areaGuid);
        notificationEntity.setSeatGuid(seatGuid);
        notificationEntity = posStoreNotificationRepository.save(notificationEntity);

        StoreOrderNotificationEntity orderNotification = new StoreOrderNotificationEntity();
        orderNotification.setStoreOrderType(StoreOrderType.GUEST);
        orderNotification.setOrderProductGroup(orderProductGroup);
        orderNotification.setNumberOfProduct(numberOfProduct);
        orderNotification.setNumberOfProduct(numberOfProduct);
        orderNotification.setOrderGuid(orderGuid);
        orderNotification.setStoreNotificationGuid(notificationGuid);
        orderNotification = posStoreOrderNotificationRepository.save(orderNotification);

        return new PosStoreNotificationDTO(notificationEntity, orderNotification);
    }

    public void toggleVisibility(PosStoreNotificationVisibilityUpdateDTO payload, String currentUser) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<StoreNotificationEntity> list = posStoreNotificationRepository.findByGuidIn(payload.getListGuid());

        list.forEach(item -> {
            if (!item.getStoreGuid().equals(payload.getStoreGuid()))
                throw new BadRequestException(ErrorCode.NOTIFICATION_NOT_IN_STORE);
        });

        list = list.stream().peek(item -> {
            item.setStoreNotificationHidden(payload.isHidden());
            if (item.getFirstHiddenBy() == null && payload.isHidden()) {
                item.setFirstHiddenBy(currentUser);
                item.setFirstHiddenDate(Instant.now());
            }
        }).collect(Collectors.toList());

        posStoreNotificationRepository.saveAll(list);
    }

    public List<PosStoreNotificationDTO> getListStoreNotification(String storeGuid, Instant startDate, StoreNotificationType type, Boolean hidden) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        List<PosStoreNotificationDTO> list = new ArrayList<>();
        if (type == null){
            list = posStoreNotificationRepository
                    .findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate)
                    .stream().map(PosStoreNotificationDTO::new)
                    .collect(Collectors.toList());
            list.addAll(posStoreNotificationRepository.findListPosStoreOrderNotificationDTO(UUID.fromString(storeGuid), startDate));
            list.addAll(posStoreNotificationRepository.findListPosStorePayRequestNotificationDTO(UUID.fromString(storeGuid), startDate));
        } else {
            switch (type) {
                case CALL_WAITER:
                    list = posStoreNotificationRepository
                            .findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate)
                            .stream().map(PosStoreNotificationDTO::new)
                            .collect(Collectors.toList());
                    break;
                case ORDER_UPDATE:
                    list = posStoreNotificationRepository.findListPosStoreOrderNotificationDTO(UUID.fromString(storeGuid), startDate);
                    break;
                case PAY_REQUEST:
                    list = posStoreNotificationRepository.findListPosStorePayRequestNotificationDTO(UUID.fromString(storeGuid), startDate);
                    break;
            }
        }
        if (hidden != null) {
            if (hidden) {
                list = list.stream().filter(PosStoreNotificationDTO::isStoreNotificationHidden).collect(Collectors.toList());
            } else {
                list = list.stream().filter(item -> !item.isStoreNotificationHidden()).collect(Collectors.toList());
            }
        }
        return list;
    }

    public PosStoreNotificationDTO updateStoreNotification(PosStoreNotificationDTO payload, String currentUser) {
        StoreNotificationEntity entity = posStoreNotificationRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOTIFICATION_NOT_FOUND));
        posStoreSecurity.blockAccessIfNotInStore(entity.getStoreGuid());
        entity.setStoreNotificationStatus(payload.getStoreNotificationStatus());
        if (entity.getFirstSeenBy() == null && payload.getStoreNotificationStatus().equals(StoreNotificationStatus.SEEN)){
            entity.setFirstSeenBy(currentUser);
            entity.setFirstSeenDate(Instant.now());
        }
        payload.assignProperty(posStoreNotificationRepository.save(entity));
        return payload;
    }
}
