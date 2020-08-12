package vn.com.buaansach.web.guest.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.StorePayRequestStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.StorePayRequestEntity;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class GuestStorePayRequestDTO extends AuditDTO {
    private UUID guid;
    private StorePayRequestStatus storePayRequestStatus;
    private Instant seenAt;
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;
    private UUID orderGuid;
    private String payNote;
    private int payAmount;

    /* additional */
    @Pattern(regexp = Constants.PHONE_REGEX)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String customerPhone;

    public GuestStorePayRequestDTO() {
    }

    public GuestStorePayRequestDTO(StorePayRequestEntity entity) {
        this.guid = entity.getGuid();
        this.storePayRequestStatus = entity.getStorePayRequestStatus();
        this.seenAt = entity.getSeenAt();
        this.storeGuid = entity.getStoreGuid();
        this.areaGuid = entity.getAreaGuid();
        this.seatGuid = entity.getSeatGuid();
        this.orderGuid = entity.getOrderGuid();
        this.payNote = entity.getPayNote();
        this.payAmount = entity.getPayAmount();

        this.createdDate = entity.getCreatedDate();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
