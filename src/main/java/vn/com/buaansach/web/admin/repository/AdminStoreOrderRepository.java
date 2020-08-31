package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreOrderRepository extends JpaRepository<StoreOrderNotificationEntity, Long> {
    List<StoreOrderNotificationEntity> findByStoreGuid(UUID storeGuid);
    List<StoreOrderNotificationEntity> findByAreaGuid(UUID areaGuid);
    List<StoreOrderNotificationEntity> findBySeatGuid(UUID seatGuid);
}
