package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.PaymentMethod;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;

import java.util.UUID;

@Data
@NoArgsConstructor
public class GuestStorePayRequestDTO {
    private int storePayRequestAmount;
    private PaymentMethod storePayRequestMethod;
    private String storePayRequestNote;
    private int numberOfExtraSeat;
    private String listExtraSeat;
    private String listExtraOrder;
    private UUID storeNotificationGuid;

    private UUID orderGuid;

    public GuestStorePayRequestDTO(StoreNotificationEntity notificationEntity, StorePayRequestNotificationEntity payRequest) {
        this.storePayRequestAmount = payRequest.getStorePayRequestAmount();
        this.storePayRequestMethod = payRequest.getStorePayRequestMethod();
        this.storePayRequestNote = payRequest.getStorePayRequestNote();
        this.numberOfExtraSeat = payRequest.getNumberOfExtraSeat();
        this.listExtraSeat = payRequest.getListExtraSeat();
        this.listExtraOrder = payRequest.getListExtraOrder();
        this.storeNotificationGuid = payRequest.getStoreNotificationGuid();

        this.orderGuid = notificationEntity.getOrderGuid();
    }
}
