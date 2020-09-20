package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PosPurchaseGroupDTO {
    private List<UUID> listSeatGuid = new ArrayList<>();
    private String paymentNote;
}
