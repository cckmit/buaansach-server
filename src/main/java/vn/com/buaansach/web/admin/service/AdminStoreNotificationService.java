package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;
import vn.com.buaansach.web.admin.repository.notification.AdminStoreNotificationRepository;
import vn.com.buaansach.web.admin.repository.notification.AdminStoreOrderNotificationRepository;
import vn.com.buaansach.web.admin.repository.notification.AdminStorePayRequestNotificationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStoreNotificationService {
    private final AdminStoreNotificationRepository adminStoreNotificationRepository;
    private final AdminStorePayRequestNotificationRepository adminStorePayRequestNotificationRepository;
    private final AdminStoreOrderNotificationRepository adminStoreOrderNotificationRepository;

    @Transactional
    public void deleteNotificationByStore(UUID storeGuid){
        List<StoreNotificationEntity> listStoreNotification = adminStoreNotificationRepository.findByStoreGuid(storeGuid);
        deleteNotification(listStoreNotification);
    }

    @Transactional
    public void deleteNotificationByArea(UUID areaGuid){
        List<StoreNotificationEntity> listStoreNotification = adminStoreNotificationRepository.findByAreaGuid(areaGuid);
        deleteNotification(listStoreNotification);
    }

    @Transactional
    public void deleteNotificationBySeat(UUID seatGuid){
        List<StoreNotificationEntity> listStoreNotification = adminStoreNotificationRepository.findBySeatGuid(seatGuid);
        deleteNotification(listStoreNotification);
    }

    private void deleteNotification(List<StoreNotificationEntity> listStoreNotification){
        List<UUID> listNotificationGuid = listStoreNotification.stream().map(StoreNotificationEntity::getGuid).collect(Collectors.toList());
        List<StorePayRequestNotificationEntity> listPayRequest = adminStorePayRequestNotificationRepository.findByStoreNotificationGuidIn(listNotificationGuid);
        List<StoreOrderNotificationEntity> listStoreOrder = adminStoreOrderNotificationRepository.findByStoreNotificationGuidIn(listNotificationGuid);
        adminStorePayRequestNotificationRepository.deleteAll(listPayRequest);
        adminStoreOrderNotificationRepository.deleteAll(listStoreOrder);
        adminStoreNotificationRepository.deleteAll(listStoreNotification);
    }
}
