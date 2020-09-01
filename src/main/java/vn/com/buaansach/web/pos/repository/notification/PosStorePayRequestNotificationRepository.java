package vn.com.buaansach.web.pos.repository.notification;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.core.repository.notification.StorePayRequestNotificationRepository;

@Repository
public interface PosStorePayRequestNotificationRepository extends StorePayRequestNotificationRepository {
//    List<StorePayRequestNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID storeGuid, Instant from);
//
//    List<StorePayRequestNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID storeGuid, Instant from, boolean hidden);
//
//    Optional<StorePayRequestNotificationEntity> findOneByGuid(UUID guid);
//
//    List<StorePayRequestNotificationEntity> findByGuidIn(List<UUID> listGuid);
//
//    List<StorePayRequestNotificationEntity> findByCreatedDateBefore(Instant before);
//
//    Optional<StorePayRequestNotificationEntity> findOneByOrderGuid(UUID orderGuid);
}
