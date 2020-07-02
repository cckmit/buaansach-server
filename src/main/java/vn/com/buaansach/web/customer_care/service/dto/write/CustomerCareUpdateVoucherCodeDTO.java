package vn.com.buaansach.web.customer_care.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.VoucherCodeClaimStatus;

@Data
public class CustomerCareUpdateVoucherCodeDTO {
    private String voucherCode;
    private VoucherCodeClaimStatus voucherCodeClaimStatus;
}
