package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.DiscountType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class AdminUpdateVoucherDTO {
    private UUID guid;
    @NotNull
    private String voucherName;
    private String voucherDescription;
    private int voucherDiscount;
    private DiscountType voucherDiscountType;
}
