package vn.com.buaansach.web.admin.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class AdminAreaDTO {
    private UUID guid;
    private String areaName;
    private List<SeatEntity> listSeat = new ArrayList<>();

    /* audit attributes */
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

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
