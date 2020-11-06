package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GuestCreateOrderDTO {
    private UUID storeGuid;
    private UUID seatGuid;
    private List<GuestOrderProductDTO> listOrderProduct = new ArrayList<>();
}
