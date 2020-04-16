package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminAreaDTO extends AuditDTO {
    private String areaName;
    private List<SeatEntity> listSeat = new ArrayList<>();

    public AdminAreaDTO(AreaEntity entity){
        this.guid = entity.getGuid();
        this.areaName = entity.getAreaName();
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }

    public AdminAreaDTO(AreaEntity entity, List<SeatEntity> listSeat){
        this.guid = entity.getGuid();
        this.areaName = entity.getAreaName();
        this.listSeat = listSeat;
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
