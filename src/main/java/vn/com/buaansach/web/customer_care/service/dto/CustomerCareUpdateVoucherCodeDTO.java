package vn.com.buaansach.web.customer_care.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;

@Data
public class CustomerCareUpdateVoucherCodeDTO {
    private String voucherCode;
    private VoucherCodeSentStatus voucherCodeSentStatus;
}
