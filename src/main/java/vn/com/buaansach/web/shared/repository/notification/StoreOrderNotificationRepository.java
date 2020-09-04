package vn.com.buaansach.web.shared.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;

@Repository
public interface StoreOrderNotificationRepository extends JpaRepository<StoreOrderNotificationEntity, Long> {

}
