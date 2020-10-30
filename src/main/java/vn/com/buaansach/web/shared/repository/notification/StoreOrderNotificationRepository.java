package vn.com.buaansach.web.shared.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreOrderNotificationRepository extends JpaRepository<StoreOrderNotificationEntity, Long> {
    List<StoreOrderNotificationEntity> findByStoreNotificationGuidIn(List<UUID> listNotificationGuid);
}
