package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.AreaType;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.core.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminAreaDTO extends AuditDTO {
    private UUID guid;

    @Size(min = 1, max = 50)
    private String areaName;

    @Enumerated(EnumType.STRING)
    private AreaType areaType;

    @Size(min = 1, max = 50)
    private String areaColor;

    private boolean areaActivated;

    private List<SeatEntity> listSeat = new ArrayList<>();

    public AdminAreaDTO() {
    }

    public AdminAreaDTO(AreaEntity entity, List<SeatEntity> listSeat) {
        this.guid = entity.getGuid();
        this.areaName = entity.getAreaName();
        this.areaType = entity.getAreaType();
        this.areaColor = entity.getAreaColor();
        this.areaActivated = entity.isAreaActivated();
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();

        this.listSeat = listSeat;
    }
}
