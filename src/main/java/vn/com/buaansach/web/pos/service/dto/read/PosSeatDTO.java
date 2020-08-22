package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.AreaType;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PosSeatDTO {
    private UUID guid;
    private String seatName;
    private SeatStatus seatStatus;
    private SeatServiceStatus seatServiceStatus;
    private boolean seatLocked;
    private UUID orderGuid;
    private UUID areaGuid;

    /* Additional Info */
    private String areaName;
    private AreaType areaType;
    private String areaColor;

    public PosSeatDTO(SeatEntity seatEntity, AreaEntity areaEntity) {
        this.guid = seatEntity.getGuid();
        this.seatName = seatEntity.getSeatName();
        this.seatStatus = seatEntity.getSeatStatus();
        this.seatServiceStatus = seatEntity.getSeatServiceStatus();
        this.seatLocked = seatEntity.isSeatLocked();
        this.orderGuid = seatEntity.getOrderGuid();
        this.areaGuid = areaEntity.getGuid();

        this.areaName = areaEntity.getAreaName();
        this.areaType = areaEntity.getAreaType();
        this.areaColor = areaEntity.getAreaColor();
    }
}
