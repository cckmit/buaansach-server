package vn.com.buaansach.web.guest.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.OrderFeedbackAction;
import vn.com.buaansach.entity.enumeration.ProductQualityRating;
import vn.com.buaansach.entity.enumeration.ServiceQualityRating;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class GuestOrderFeedbackDTO {
    private UUID orderGuid;
    @NotNull
    private OrderFeedbackAction orderFeedbackAction;
    private ServiceQualityRating serviceQualityRating;
    private ProductQualityRating productQualityRating;
    @Size(max = 500)
    private String feedbackContent;
}
