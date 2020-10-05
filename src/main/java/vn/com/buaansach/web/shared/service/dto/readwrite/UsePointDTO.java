package vn.com.buaansach.web.shared.service.dto.readwrite;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UsePointDTO {
    private UUID orderGuid;
    private int orderPointValue;
    // send notification
    private int orderPointCost;

    public UsePointDTO(int orderPointValue, int orderPointCost) {
        this.orderPointValue = orderPointValue;
        this.orderPointCost = orderPointCost;
    }
}
