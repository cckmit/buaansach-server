package vn.com.buaansach.shared.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreNotificationRepository extends JpaRepository<StoreNotificationEntity, Long> {

    List<StoreNotificationEntity> findByGuidIn(List<UUID> listGuid);

    List<StoreNotificationEntity> findByCreatedDateBefore(Instant deletePoint);

    Optional<StoreNotificationEntity> findOneByGuid(UUID guid);
}
