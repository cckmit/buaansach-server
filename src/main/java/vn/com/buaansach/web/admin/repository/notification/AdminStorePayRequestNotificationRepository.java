package vn.com.buaansach.web.admin.repository.notification;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.notification.StorePayRequestNotificationRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStorePayRequestNotificationRepository extends StorePayRequestNotificationRepository {
    void deleteByStoreNotificationGuidIn(List<UUID> listGuid);
}
