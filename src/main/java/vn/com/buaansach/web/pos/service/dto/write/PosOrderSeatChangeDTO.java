package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderSeatChangeDTO {
    @NotBlank
    private UUID currentSeatGuid;
    @NotBlank
    private UUID newSeatGuid;
    @NotBlank
    private UUID orderGuid;
    @NotBlank
    @Size(max = 255)
    private String changeReason;
}
