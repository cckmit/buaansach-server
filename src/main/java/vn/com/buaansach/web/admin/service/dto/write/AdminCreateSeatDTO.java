package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AdminCreateSeatDTO {
    /*used for admin and manager*/
    private UUID areaGuid;

    @Size(min = 1, max = 50)
    private String seatName;

    @Size(min = 1, max = 50)
    private String seatNameEng;
}
