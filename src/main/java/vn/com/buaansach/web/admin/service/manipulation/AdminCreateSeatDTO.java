package vn.com.buaansach.web.admin.service.manipulation;

import lombok.Data;

import java.util.UUID;

@Data
public class AdminCreateSeatDTO {
    /*used for admin and manager*/
    private UUID areaGuid;
    private String seatName;
}
