package vn.com.buaansach.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.enumeration.StoreOrderType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.shared.repository.notification.StoreNotificationRepository;
import vn.com.buaansach.shared.repository.notification.StoreOrderNotificationRepository;
import vn.com.buaansach.shared.repository.notification.StorePayRequestNotificationRepository;
import vn.com.buaansach.shared.service.dto.StoreNotificationDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreNotificationService {
    private final StoreNotificationRepository storeNotificationRepository;
    private final StoreOrderNotificationRepository storeOrderNotificationRepository;
    private final StorePayRequestNotificationRepository storePayRequestNotificationRepository;

    public StoreNotificationDTO createStoreOrderUpdateNotification(UUID storeGuid, UUID areaGuid, UUID seatGuid, UUID orderGuid, UUID orderProductGroup, int numberOfProduct) {
        StoreNotificationEntity notificationEntity = new StoreNotificationEntity();
        UUID notificationGuid = UUID.randomUUID();
        notificationEntity.setGuid(notificationGuid);
        notificationEntity.setStoreNotificationStatus(StoreNotificationStatus.UNSEEN);
        notificationEntity.setStoreNotificationType(StoreNotificationType.ORDER_UPDATE);
        notificationEntity.setStoreNotificationHidden(false);
        notificationEntity.setStoreGuid(storeGuid);
        notificationEntity.setAreaGuid(areaGuid);
        notificationEntity.setSeatGuid(seatGuid);
        notificationEntity = storeNotificationRepository.save(notificationEntity);

        StoreOrderNotificationEntity orderNotification = new StoreOrderNotificationEntity();
        orderNotification.setStoreOrderType(StoreOrderType.GUEST);
        orderNotification.setOrderProductGroup(orderProductGroup);
        orderNotification.setNumberOfProduct(numberOfProduct);
        orderNotification.setNumberOfProduct(numberOfProduct);
        orderNotification.setStoreNotificationGuid(notificationGuid);
        orderNotification = storeOrderNotificationRepository.save(orderNotification);

        return new StoreNotificationDTO(notificationEntity, orderNotification);
    }
}
