package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.web.guest.repository.store.GuestStoreOrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreOrderService {
    private final GuestStoreOrderRepository guestStoreOrderRepository;

    public StoreOrderNotificationEntity createStoreOrder(UUID storeGuid, UUID areaGuid, UUID seatGuid, UUID orderGuid, UUID orderProductGroup, int numberOfProduct) {
        StoreOrderNotificationEntity storeOrderNotificationEntity = new StoreOrderNotificationEntity();
//        storeOrderNotificationEntity.setGuid(UUID.randomUUID());
//        storeOrderNotificationEntity.setStoreOrderStatus(StoreOrderStatus.UNSEEN);
//        storeOrderNotificationEntity.setStoreOrderType(StoreOrderType.GUEST);
//        storeOrderNotificationEntity.setStoreOrderHidden(false);
//        storeOrderNotificationEntity.setStoreGuid(storeGuid);
//        storeOrderNotificationEntity.setAreaGuid(areaGuid);
//        storeOrderNotificationEntity.setSeatGuid(seatGuid);
//        storeOrderNotificationEntity.setOrderGuid(orderGuid);
        storeOrderNotificationEntity.setOrderProductGroup(orderProductGroup);
        storeOrderNotificationEntity.setNumberOfProduct(numberOfProduct);
        return guestStoreOrderRepository.save(storeOrderNotificationEntity);
    }
}
