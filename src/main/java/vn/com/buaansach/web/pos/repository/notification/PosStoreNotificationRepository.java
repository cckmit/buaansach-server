package vn.com.buaansach.web.pos.repository.notification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.web.shared.repository.notification.StoreNotificationRepository;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreNotificationDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface PosStoreNotificationRepository extends StoreNotificationRepository {
    List<StoreNotificationEntity> findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID storeGuid, Instant startDate);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreNotificationDTO(notification, payRequest) " +
            "FROM StoreNotificationEntity notification " +
            "JOIN vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity payRequest " +
            "ON notification.guid = payRequest.storeNotificationGuid " +
            "WHERE notification.storeGuid = :storeGuid " +
            "AND notification.createdDate >= :startDate " +
            "ORDER BY notification.createdDate ASC ")
    List<PosStoreNotificationDTO> findListPosStorePayRequestNotificationDTO(@Param("storeGuid") UUID storeGuid, @Param("startDate") Instant startDate);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreNotificationDTO(notification, storeOrder) " +
            "FROM StoreNotificationEntity notification " +
            "JOIN vn.com.buaansach.entity.notification.StoreOrderNotificationEntity storeOrder " +
            "ON notification.guid = storeOrder.storeNotificationGuid " +
            "WHERE notification.storeGuid = :storeGuid " +
            "AND notification.createdDate >= :startDate " +
            "ORDER BY notification.createdDate ASC ")
    List<PosStoreNotificationDTO> findListPosStoreOrderNotificationDTO(@Param("storeGuid") UUID storeGuid, @Param("startDate") Instant startDate);
}