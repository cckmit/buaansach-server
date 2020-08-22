package vn.com.buaansach.web.guest.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.StorePayRequestStatus;
import vn.com.buaansach.entity.store.StorePayRequestEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GuestStorePayRequestDTO extends AuditDTO {
    private UUID guid;
    private StorePayRequestStatus storePayRequestStatus;
    private boolean storePayRequestHidden;
    private int storePayRequestAmount;
    private String storePayRequestNote;
    private String firstSeenBy;
    private Instant firstSeenDate;
    private String firstHiddenBy;
    private Instant firstHiddenDate;
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;
    private UUID orderGuid;

    public GuestStorePayRequestDTO(StorePayRequestEntity entity) {
        this.guid = entity.getGuid();
        this.storePayRequestStatus = entity.getStorePayRequestStatus();
        this.storePayRequestHidden = entity.isStorePayRequestHidden();
        this.storePayRequestAmount = entity.getStorePayRequestAmount();
        this.storePayRequestNote = entity.getStorePayRequestNote();

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
