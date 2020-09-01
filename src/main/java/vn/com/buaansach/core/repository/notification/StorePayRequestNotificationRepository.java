package vn.com.buaansach.core.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;

@Repository
public interface StorePayRequestNotificationRepository extends JpaRepository<StorePayRequestNotificationEntity, Long> {

}
