package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.entity.enumeration.SeatStatus;

import java.util.UUID;

@Data
public class PosSeatDTO {
    private UUID guid;
    private String seatName;
    private SeatStatus seatStatus;
    private UUID currentOrderGuid;

    private String areaName;
    private UUID areaGuid;

    public PosSeatDTO() {
    }

    public PosSeatDTO(SeatEntity seatEntity, AreaEntity areaEntity) {
        this.guid = seatEntity.getGuid();
        this.seatName = seatEntity.getSeatName();
        this.seatStatus = seatEntity.getSeatStatus();
        this.currentOrderGuid = seatEntity.getCurrentOrderGuid();
        this.areaGuid = areaEntity.getGuid();
        this.areaName = areaEntity.getAreaName();
    }
}
