package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderCancelDTO {
    private UUID orderGuid;
    @NotBlank
    @Size(min = 1, max = 255)
    private String cancelReason;
}
