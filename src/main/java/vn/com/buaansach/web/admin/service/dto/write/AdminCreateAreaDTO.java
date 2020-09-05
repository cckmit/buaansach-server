package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.AreaType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AdminCreateAreaDTO {
    private UUID storeGuid;

    @Size(max = 50)
    private String areaName;

    @Size(max = 50)
    private String areaNameEng;

    private AreaType areaType;

    @Size(max = 50)
    private String areaColor;

    private boolean areaActivated;

    @Size(max = 40)
    private String seatPrefix;

    @Size(max = 40)
    private String seatPrefixEng;

    private int numberOfSeats;
}
