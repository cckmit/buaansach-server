package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class GuestCreateOrderDTO {
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;

//    @Size(max = 100)
//    private String customerName;
//
//    @Pattern(regexp = Constants.PHONE_REGEX)
//    private String customerPhone;
}
