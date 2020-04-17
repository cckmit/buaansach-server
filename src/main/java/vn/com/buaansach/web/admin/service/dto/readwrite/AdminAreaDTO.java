package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

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

    private List<SeatEntity> listSeat = new ArrayList<>();

    public AdminAreaDTO(AreaEntity entity, List<SeatEntity> listSeat) {
        this.guid = entity.getGuid();
        this.areaName = entity.getAreaName();
        this.listSeat = listSeat;
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
