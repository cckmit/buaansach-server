package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.OrderFeedbackAction;
import vn.com.buaansach.entity.enumeration.ProductQualityRating;
import vn.com.buaansach.entity.enumeration.ServiceQualityRating;
import vn.com.buaansach.entity.order.OrderFeedbackEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PosOrderFeedbackDTO {
    private UUID guid;
    private UUID storeGuid;
    private UUID orderGuid;
    @NotNull
    private OrderFeedbackAction orderFeedbackAction;
    private ServiceQualityRating serviceQualityRating;
    private ProductQualityRating productQualityRating;
    @Size(max = 500)
    private String feedbackContent;

    public PosOrderFeedbackDTO() {
    }

    public PosOrderFeedbackDTO(OrderFeedbackEntity entity) {
        this.guid = entity.getGuid();
        this.storeGuid = entity.getStoreGuid();
        this.orderGuid = entity.getOrderGuid();
        this.orderFeedbackAction = entity.getOrderFeedbackAction();
        this.serviceQualityRating = entity.getServiceQualityRating();
        this.productQualityRating = entity.getProductQualityRating();
        this.feedbackContent = entity.getFeedbackContent();
    }
}
