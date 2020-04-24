package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PosOrderUpdateDTO {
    private UUID orderGuid;
    private List<PosOrderProductDTO> listOrderProduct = new ArrayList<>();
}
