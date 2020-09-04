package vn.com.buaansach.shared.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoreNotificationDTO extends AuditDTO {
    private UUID guid;
    private StoreNotificationStatus storeNotificationStatus;
    private StoreNotificationType storeNotificationType;
    private boolean storeNotificationHidden;
    private String firstSeenBy;
    private Instant firstSeenDate;
    private String firstHiddenBy;
    private Instant firstHiddenDate;
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;

    private StoreOrderNotificationEntity orderNotification;
    private StorePayRequestNotificationEntity payRequestNotification;

    public StoreNotificationDTO() {
    }

    public StoreNotificationDTO(StoreNotificationEntity entity) {
        assignProperty(entity);
    }

    public StoreNotificationDTO(StoreNotificationEntity entity, StoreOrderNotificationEntity orderNotification) {
        assignProperty(entity);
        this.orderNotification = orderNotification;
    }

    public StoreNotificationDTO(StoreNotificationEntity entity, StorePayRequestNotificationEntity payRequestNotification) {
        assignProperty(entity);
        this.payRequestNotification = payRequestNotification;
    }

    private void assignProperty(StoreNotificationEntity entity) {
        this.guid = entity.getGuid();
        this.storeNotificationStatus = entity.getStoreNotificationStatus();
        this.storeNotificationType = entity.getStoreNotificationType();
        this.storeNotificationHidden = entity.isStoreNotificationHidden();
        this.firstSeenBy = entity.getFirstSeenBy();
        this.firstSeenDate = entity.getFirstSeenDate();
        this.firstHiddenBy = entity.getFirstHiddenBy();
        this.firstHiddenDate = entity.getFirstHiddenDate();
        this.storeGuid = entity.getStoreGuid();
        this.areaGuid = entity.getAreaGuid();
        this.seatGuid = entity.getSeatGuid();

        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
