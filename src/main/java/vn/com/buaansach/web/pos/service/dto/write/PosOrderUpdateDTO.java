package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PosOrderUpdateDTO {
    private UUID orderGuid;
    @Pattern(regexp = Constants.PHONE_REGEX)
    private List<PosOrderProductDTO> listOrderProduct = new ArrayList<>();
}
