package vn.com.buaansach.web.pos.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;
import vn.com.buaansach.entity.enumeration.StoreOrderType;
import vn.com.buaansach.entity.enumeration.StorePayRequestStatus;
import vn.com.buaansach.entity.store.StoreOrderEntity;
import vn.com.buaansach.entity.store.StorePayRequestEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class PosStorePayRequestDTO extends AuditDTO {
    private UUID guid;
    private StorePayRequestStatus storePayRequestStatus;
    private Instant seenAt;
    private String firstSeenBy;
    private String firstHideBy;
    private boolean hidden;
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;
    private UUID orderGuid;
    private String payNote;
    private int payAmount;

    public PosStorePayRequestDTO() {
    }

    public PosStorePayRequestDTO(StorePayRequestEntity entity) {
        this.guid = entity.getGuid();
        this.storePayRequestStatus = entity.getStorePayRequestStatus();
        this.seenAt = entity.getSeenAt();
        this.firstSeenBy = entity.getFirstSeenBy();
        this.firstHideBy = entity.getFirstHideBy();
        this.hidden = entity.isHidden();
        this.storeGuid = entity.getStoreGuid();
        this.areaGuid = entity.getAreaGuid();
        this.seatGuid = entity.getSeatGuid();
        this.orderGuid = entity.getOrderGuid();
        this.payNote = entity.getPayNote();
        this.payAmount = entity.getPayAmount();

        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
