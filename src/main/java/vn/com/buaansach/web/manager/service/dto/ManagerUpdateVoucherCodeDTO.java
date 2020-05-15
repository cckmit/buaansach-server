package vn.com.buaansach.web.manager.service.dto;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;

@Data
public class ManagerUpdateVoucherCodeDTO {
    private String voucherCode;
    private VoucherCodeSentStatus voucherCodeSentStatus;
}
