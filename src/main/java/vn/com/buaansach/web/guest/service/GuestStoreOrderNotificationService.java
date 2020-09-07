package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.enumeration.StoreOrderType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;
import vn.com.buaansach.web.guest.repository.notification.GuestStoreNotificationRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreOrderNotificationRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreNotificationDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreOrderNotificationService {
    private final GuestStoreOrderNotificationRepository guestStoreOrderNotificationRepository;
    private final GuestStoreNotificationRepository guestStoreNotificationRepository;

    public GuestStoreNotificationDTO createStoreOrderUpdateNotification(UUID storeGuid, UUID areaGuid, UUID seatGuid, UUID orderGuid, UUID orderProductGroup, int numberOfProduct) {
        StoreNotificationEntity notificationEntity = new StoreNotificationEntity();
        UUID notificationGuid = UUID.randomUUID();
        notificationEntity.setGuid(notificationGuid);
        notificationEntity.setStoreNotificationStatus(StoreNotificationStatus.UNSEEN);
        notificationEntity.setStoreNotificationType(StoreNotificationType.ORDER_UPDATE);
        notificationEntity.setStoreNotificationHidden(false);
        notificationEntity.setStoreGuid(storeGuid);
        notificationEntity.setAreaGuid(areaGuid);
        notificationEntity.setSeatGuid(seatGuid);
        notificationEntity = guestStoreNotificationRepository.save(notificationEntity);

        StoreOrderNotificationEntity orderNotification = new StoreOrderNotificationEntity();
        orderNotification.setStoreOrderType(StoreOrderType.GUEST);
        orderNotification.setOrderProductGroup(orderProductGroup);
        orderNotification.setNumberOfProduct(numberOfProduct);
        orderNotification.setNumberOfProduct(numberOfProduct);
        orderNotification.setOrderGuid(orderGuid);
        orderNotification.setStoreNotificationGuid(notificationGuid);
        orderNotification = guestStoreOrderNotificationRepository.save(orderNotification);

        return new GuestStoreNotificationDTO(notificationEntity, orderNotification);
    }
}
