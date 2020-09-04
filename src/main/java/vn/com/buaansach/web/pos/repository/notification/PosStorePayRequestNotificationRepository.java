package vn.com.buaansach.web.pos.repository.notification;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;
import vn.com.buaansach.shared.repository.notification.StorePayRequestNotificationRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosStorePayRequestNotificationRepository extends StorePayRequestNotificationRepository {
    List<StorePayRequestNotificationEntity> findByStoreNotificationGuidIn(List<UUID> listGuid);
    void deleteByStoreNotificationGuidIn(List<UUID> listGuid);
}
