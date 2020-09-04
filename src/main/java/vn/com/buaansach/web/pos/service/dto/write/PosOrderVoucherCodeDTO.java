package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderVoucherCodeDTO {
    private UUID orderGuid;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String customerPhone;

    @Size(min = 6, max = 20)
    private String voucherCode;
}
