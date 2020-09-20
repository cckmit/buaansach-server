package vn.com.buaansach.web.pos.service.dto.readwrite;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PosListOrderDTO {
    private UUID storeGuid;
    private List<UUID> listSeatGuid;
}
