package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;
import vn.com.buaansach.entity.enumeration.StorePayRequestStatus;

import java.util.UUID;

@Data
public class PosStorePayRequestStatusUpdateDTO {
    private UUID guid;
    private StorePayRequestStatus storePayRequestStatus;
}
