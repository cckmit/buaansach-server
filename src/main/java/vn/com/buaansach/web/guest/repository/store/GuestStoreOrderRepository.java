package vn.com.buaansach.web.guest.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;

@Repository
public interface GuestStoreOrderRepository extends JpaRepository<StoreOrderNotificationEntity, Long> {
}
