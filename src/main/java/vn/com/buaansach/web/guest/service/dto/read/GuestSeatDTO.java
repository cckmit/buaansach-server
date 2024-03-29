package vn.com.buaansach.web.guest.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.UUID;

@Data
public class GuestSeatDTO {
    private UUID guid;
    private String seatName;
    private String seatNameEng;
    private SeatStatus seatStatus;
    private SeatServiceStatus seatServiceStatus;
    private boolean seatLocked;

    private UUID areaGuid;
    private String areaName;
    private String areaNameEng;
    private boolean areaActivated;

    private UUID storeGuid;
    private String storeName;
    private StoreStatus storeStatus;

    public GuestSeatDTO() {
    }

    public GuestSeatDTO(StoreEntity storeEntity, AreaEntity areaEntity, SeatEntity seatEntity) {
        this.guid = seatEntity.getGuid();
        this.seatName = seatEntity.getSeatName();
        this.seatNameEng = seatEntity.getSeatNameEng();
        this.seatStatus = seatEntity.getSeatStatus();
        this.seatServiceStatus = seatEntity.getSeatServiceStatus();
        this.seatLocked = seatEntity.isSeatLocked();

        this.areaGuid = areaEntity.getGuid();
        this.areaName = areaEntity.getAreaName();
        this.areaNameEng = areaEntity.getAreaNameEng();
        this.areaActivated = areaEntity.isAreaActivated();

        this.storeGuid = storeEntity.getGuid();
        this.storeName = storeEntity.getStoreName();
        this.storeStatus = storeEntity.getStoreStatus();
    }
}
