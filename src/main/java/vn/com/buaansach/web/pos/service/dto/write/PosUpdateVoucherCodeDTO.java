package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.VoucherCodeClaimStatus;

@Data
public class PosUpdateVoucherCodeDTO {
    private String voucherCode;
    private VoucherCodeClaimStatus voucherCodeClaimStatus;
}
