package vn.com.buaansach.web.guest.service.dto.readwrite;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;

import java.util.UUID;

@Data
@NoArgsConstructor
public class GuestStorePayRequestDTO {
    private int storePayRequestAmount;
    private String storePayRequestNote;
    private int numberOfExtraSeat;
    private String listExtraSeat;
    private String listExtraOrder;
    private UUID orderGuid;
    private UUID storeNotificationGuid;

    public GuestStorePayRequestDTO(StorePayRequestNotificationEntity entity) {
        this.storePayRequestAmount = entity.getStorePayRequestAmount();
        this.storePayRequestNote = entity.getStorePayRequestNote();
        this.numberOfExtraSeat = entity.getNumberOfExtraSeat();
        this.listExtraSeat = entity.getListExtraSeat();
        this.listExtraOrder = entity.getListExtraOrder();
        this.orderGuid = entity.getOrderGuid();
        this.storeNotificationGuid = entity.getStoreNotificationGuid();
    }
}
