package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PosStorePayRequestRepository extends JpaRepository<StorePayRequestNotificationEntity, Long> {
    List<StorePayRequestNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID storeGuid, Instant from);

    List<StorePayRequestNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID storeGuid, Instant from, boolean hidden);

    Optional<StorePayRequestNotificationEntity> findOneByGuid(UUID guid);

    List<StorePayRequestNotificationEntity> findByGuidIn(List<UUID> listGuid);

    List<StorePayRequestNotificationEntity> findByCreatedDateBefore(Instant before);

    Optional<StorePayRequestNotificationEntity> findOneByOrderGuid(UUID orderGuid);
}
