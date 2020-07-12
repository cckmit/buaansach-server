package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;

import java.util.UUID;

@Data
public class PosStoreOrderStatusUpdateDTO {
    private UUID guid;
    private StoreOrderStatus storeOrderStatus;
}
