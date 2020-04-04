package vn.com.buaansach.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class AreaDTO {
    private UUID guid;
    private String areaName;
    private List<SeatEntity> listSeat = new ArrayList<>();

    /* audit attributes */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant lastModifiedDate;

    public AreaDTO(AreaEntity entity){
        this.guid = entity.getGuid();
        this.areaName = entity.getAreaName();
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }

    public AreaDTO(AreaEntity entity, List<SeatEntity> listSeat){
        this.guid = entity.getGuid();
        this.areaName = entity.getAreaName();
        this.listSeat = listSeat;
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
