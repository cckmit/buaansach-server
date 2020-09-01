package vn.com.buaansach.web.pos.repository.notification;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.core.repository.notification.StoreOrderNotificationRepository;

@Repository
public interface PosStoreOrderNotificationRepository extends StoreOrderNotificationRepository {
//    List<StoreOrderNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID storeGuid, Instant from);
//
//    List<StoreOrderNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID storeGuid, Instant from, boolean hidden);
//
//    Optional<StoreOrderNotificationEntity> findOneByGuid(UUID guid);
//
//    List<StoreOrderNotificationEntity> findByGuidIn(List<UUID> listGuid);
//
//    List<StoreOrderNotificationEntity> findByCreatedDateBefore(Instant before);
//
//    List<StoreOrderNotificationEntity> findByOrderGuid(UUID orderGuid);
}
