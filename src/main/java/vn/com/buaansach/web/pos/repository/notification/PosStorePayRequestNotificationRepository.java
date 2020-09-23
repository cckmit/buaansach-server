package vn.com.buaansach.web.pos.repository.notification;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.notification.StorePayRequestNotificationRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosStorePayRequestNotificationRepository extends StorePayRequestNotificationRepository {
    void deleteByStoreNotificationGuidIn(List<UUID> listGuid);
}
