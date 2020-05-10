package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class GuestCancelOrderDTO {
    private UUID orderGuid;
    @Size(max = 255)
    @NotBlank
    private String cancelReason;
}
