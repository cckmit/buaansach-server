package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosStoreOrderRepository extends JpaRepository<StoreOrderNotificationEntity, Long> {
    List<StoreOrderNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID storeGuid, Instant from);

    List<StoreOrderNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID storeGuid, Instant from, boolean hidden);

    Optional<StoreOrderNotificationEntity> findOneByGuid(UUID guid);

    List<StoreOrderNotificationEntity> findByGuidIn(List<UUID> listGuid);

    List<StoreOrderNotificationEntity> findByCreatedDateBefore(Instant before);

    List<StoreOrderNotificationEntity> findByOrderGuid(UUID orderGuid);
}
