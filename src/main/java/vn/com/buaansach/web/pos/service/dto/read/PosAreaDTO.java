package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.store.AreaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PosAreaDTO {
    private UUID guid;
    private String areaName;
    private String areaColor;
    private List<PosSeatDTO> listSeat = new ArrayList<>();

    public PosAreaDTO() {
    }

    public PosAreaDTO(AreaEntity areaEntity) {
        this.guid = areaEntity.getGuid();
        this.areaName = areaEntity.getAreaName();
        this.areaColor = areaEntity.getAreaColor();
    }

    public PosAreaDTO(AreaEntity areaEntity, List<PosSeatDTO> listSeat) {
        this.guid = areaEntity.getGuid();
        this.areaName = areaEntity.getAreaName();
        this.areaColor = areaEntity.getAreaColor();
        this.listSeat = listSeat;
    }
}
