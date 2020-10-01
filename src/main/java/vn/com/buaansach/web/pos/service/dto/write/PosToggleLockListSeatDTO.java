package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PosToggleLockListSeatDTO {
    private List<UUID> listSeatGuid;
    private boolean locked;
}
