package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AdminCreateAreaDTO {
    private UUID storeGuid;
    @Size(max = 50)
    private String areaName;
    @Size(max = 50)
    private String areaColor;
    @Size(max = 40)
    private String seatPrefix;
    private int numberOfSeats;
}
