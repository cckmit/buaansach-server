package vn.com.buaansach.web.guest.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.enumeration.SeatStatus;

import java.util.UUID;

@Data
public class GuestSeatDTO {
    private UUID seatGuid;
    private UUID areaGuid;
    private UUID storeGuid;
    private String seatName;
    private String areaName;
    private String storeName;
    private SeatStatus seatStatus;

    public GuestSeatDTO() {
    }

    public GuestSeatDTO(StoreEntity storeEntity, AreaEntity areaEntity, SeatEntity seatEntity) {
        this.seatGuid = seatEntity.getGuid();
        this.seatName = seatEntity.getSeatName();
        this.seatStatus = seatEntity.getSeatStatus();
        this.areaGuid = areaEntity.getGuid();
        this.areaName = areaEntity.getAreaName();
        this.storeGuid = storeEntity.getGuid();
        this.storeName = storeEntity.getStoreName();
    }
}