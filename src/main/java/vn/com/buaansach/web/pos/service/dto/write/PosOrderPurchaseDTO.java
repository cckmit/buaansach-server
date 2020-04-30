package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.PaymentMethod;

import java.util.UUID;

@Data
public class PosOrderPurchaseDTO {
    private UUID orderGuid;
    private PaymentMethod paymentMethod;
    private int totalCharge;
}
