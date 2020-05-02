package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
public class PosOrderCreateDTO {
    private UUID seatGuid;
    private String customerName;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String customerPhone;
}
