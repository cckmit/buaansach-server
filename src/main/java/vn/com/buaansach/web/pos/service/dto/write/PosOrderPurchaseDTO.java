package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.PaymentMethod;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderPurchaseDTO {
    private UUID orderGuid;
    private int orderPointValue;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Size(max = 255)
    private String paymentNote;
}
