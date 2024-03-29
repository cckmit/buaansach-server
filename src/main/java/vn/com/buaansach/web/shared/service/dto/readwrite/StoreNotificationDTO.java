package vn.com.buaansach.web.shared.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StoreNotificationDTO extends AuditDTO {
    private UUID guid;
    private StoreNotificationStatus storeNotificationStatus;
    private StoreNotificationType storeNotificationType;
    private boolean storeNotificationHidden;
    private boolean storeNotificationPin;
    private String firstSeenBy;
    private Instant firstSeenDate;
    private String firstHiddenBy;
    private Instant firstHiddenDate;
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;
    private UUID orderGuid;

    private StoreOrderNotificationEntity orderNotification;
    private StorePayRequestNotificationEntity payRequestNotification;

    public StoreNotificationDTO(StoreNotificationEntity entity) {
        assignProperty(entity);
    }

    public StoreNotificationDTO(StoreNotificationEntity entity, StorePayRequestNotificationEntity payRequestNotification) {
        assignProperty(entity);
        this.payRequestNotification = payRequestNotification;
    }

    public StoreNotificationDTO(StoreNotificationEntity entity, StoreOrderNotificationEntity orderNotification) {
        assignProperty(entity);
        this.orderNotification = orderNotification;
    }

    public void assignProperty(StoreNotificationEntity entity){
        this.guid = entity.getGuid();
        this.storeNotificationStatus = entity.getStoreNotificationStatus();
        this.storeNotificationType = entity.getStoreNotificationType();
        this.storeNotificationHidden = entity.isStoreNotificationHidden();
        this.storeNotificationPin = entity.isStoreNotificationPin();
        this.firstSeenBy = entity.getFirstSeenBy();
        this.firstSeenDate = entity.getFirstSeenDate();
        this.firstHiddenBy = entity.getFirstHiddenBy();
        this.firstHiddenDate = entity.getFirstHiddenDate();
        this.storeGuid = entity.getStoreGuid();
        this.areaGuid = entity.getAreaGuid();
        this.seatGuid = entity.getSeatGuid();
        this.orderGuid = entity.getOrderGuid();

        this.createdDate = entity.getCreatedDate();
        this.createdBy = entity.getCreatedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
    }
}
