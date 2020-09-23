package vn.com.buaansach.web.pos.repository.notification;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.notification.StoreOrderNotificationRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosStoreOrderNotificationRepository extends StoreOrderNotificationRepository {
    void deleteByStoreNotificationGuidIn(List<UUID> listGuid);
}
