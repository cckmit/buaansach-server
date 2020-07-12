package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;
import vn.com.buaansach.entity.enumeration.StoreOrderType;
import vn.com.buaansach.entity.store.StoreOrderEntity;
import vn.com.buaansach.web.guest.repository.GuestStoreOrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreOrderService {
    private final GuestStoreOrderRepository guestStoreOrderRepository;

    public StoreOrderEntity createStoreOrder(UUID storeGuid, UUID areaGuid, UUID seatGuid, UUID orderGuid, UUID orderProductGroup, int numberOfProduct) {
        StoreOrderEntity storeOrderEntity = new StoreOrderEntity();
        storeOrderEntity.setGuid(UUID.randomUUID());
        storeOrderEntity.setStoreOrderStatus(StoreOrderStatus.UNSEEN);
        storeOrderEntity.setStoreOrderType(StoreOrderType.GUEST);
        storeOrderEntity.setHidden(false);
        storeOrderEntity.setStoreGuid(storeGuid);
        storeOrderEntity.setAreaGuid(areaGuid);
        storeOrderEntity.setSeatGuid(seatGuid);
        storeOrderEntity.setOrderGuid(orderGuid);
        storeOrderEntity.setOrderProductGroup(orderProductGroup);
        storeOrderEntity.setNumberOfProduct(numberOfProduct);
        return guestStoreOrderRepository.save(storeOrderEntity);
    }
}
