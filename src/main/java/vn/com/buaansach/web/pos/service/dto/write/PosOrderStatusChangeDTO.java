package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.PaymentMethod;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderStatusChangeDTO {
    private UUID orderGuid;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private int totalCharge;

    @Size(max = 255)
    private String cancelReason;
}
