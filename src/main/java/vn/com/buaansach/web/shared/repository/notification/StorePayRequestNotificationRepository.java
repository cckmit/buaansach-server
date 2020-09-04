package vn.com.buaansach.web.shared.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StorePayRequestNotificationRepository extends JpaRepository<StorePayRequestNotificationEntity, Long> {
    Optional<StorePayRequestNotificationEntity> findOneByOrderGuid(UUID orderGuid);
}
