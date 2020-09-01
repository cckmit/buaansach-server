package vn.com.buaansach.web.guest.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;

import java.util.Optional;
import java.util.UUID;

public interface GuestStorePayRequestNotificationRepository extends JpaRepository<StorePayRequestNotificationEntity, Long> {
    Optional<StorePayRequestNotificationEntity> findOneByOrderGuid(UUID orderGuid);
}
