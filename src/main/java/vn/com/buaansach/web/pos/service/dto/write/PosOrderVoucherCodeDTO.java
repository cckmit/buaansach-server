package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderVoucherCodeDTO {
    private UUID orderGuid;
    @Size(max = 20)
    private String customerPhone;
    @Size(min = 6, max = 20)
    private String voucherCode;
}
