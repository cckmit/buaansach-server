package vn.com.buaansach.web.pos.service.dto.write;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.OrderFeedbackAction;
import vn.com.buaansach.entity.enumeration.Rating;
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
    private Rating serviceQualityRating;
    private Rating productQualityRating;
    @Size(max = 500)
    private String orderFeedbackContent;

    public PosOrderFeedbackDTO() {
    }

    public PosOrderFeedbackDTO(OrderFeedbackEntity entity) {
        this.guid = entity.getGuid();
        this.storeGuid = entity.getStoreGuid();
        this.orderGuid = entity.getOrderGuid();
        this.orderFeedbackAction = entity.getOrderFeedbackAction();
        this.serviceQualityRating = entity.getServiceQualityRating();
        this.productQualityRating = entity.getProductQualityRating();
        this.orderFeedbackContent = entity.getOrderFeedbackContent();
    }
}
