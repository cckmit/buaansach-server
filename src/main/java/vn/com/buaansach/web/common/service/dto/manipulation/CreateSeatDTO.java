package vn.com.buaansach.web.common.service.dto.manipulation;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateSeatDTO {
    /*used for admin and manager*/
    private UUID areaGuid;
    private String seatName;
}
