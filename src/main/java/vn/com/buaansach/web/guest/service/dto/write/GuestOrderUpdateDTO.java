package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;
import vn.com.buaansach.web.guest.service.dto.GuestOrderProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GuestOrderUpdateDTO {
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID orderGuid;
    private List<GuestOrderProductDTO> listOrderProduct = new ArrayList<>();
}
