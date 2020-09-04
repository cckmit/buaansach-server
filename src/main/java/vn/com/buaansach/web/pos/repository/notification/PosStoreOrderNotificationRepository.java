package vn.com.buaansach.web.pos.repository.notification;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.shared.repository.notification.StoreOrderNotificationRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosStoreOrderNotificationRepository extends StoreOrderNotificationRepository {
    List<StoreOrderNotificationEntity> findByStoreNotificationGuidIn(List<UUID> listGuid);
    void deleteByStoreNotificationGuidIn(List<UUID> listGuid);
}
