package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderSeatChangeDTO {
    private UUID currentSeatGuid;
    private UUID newSeatGuid;
    private UUID orderGuid;
    @NotBlank
    @Size(max = 255)
    private String changeReason;
}
