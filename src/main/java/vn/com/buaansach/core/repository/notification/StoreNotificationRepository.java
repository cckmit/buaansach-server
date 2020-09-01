package vn.com.buaansach.core.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;

@Repository
public interface StoreNotificationRepository extends JpaRepository<StoreNotificationEntity, Long> {

}
