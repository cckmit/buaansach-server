package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.VoucherCodeClaimStatus;

@Data
public class AdminUpdateVoucherCodeDTO {
    String voucherCode;
    VoucherCodeClaimStatus voucherCodeClaimStatus;
}
