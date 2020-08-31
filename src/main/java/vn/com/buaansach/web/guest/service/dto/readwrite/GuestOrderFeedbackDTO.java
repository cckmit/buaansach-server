package vn.com.buaansach.web.guest.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.OrderFeedbackAction;
import vn.com.buaansach.entity.enumeration.Rating;
import vn.com.buaansach.entity.order.OrderFeedbackEntity;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class GuestOrderFeedbackDTO {
    private UUID guid;
    private UUID orderGuid;
    private OrderFeedbackAction orderFeedbackAction;
    private int serviceQualityRating;
    private int productQualityRating;
    @Size(max = 500)
    private String orderFeedbackContent;

    public GuestOrderFeedbackDTO() {
    }

    public GuestOrderFeedbackDTO(OrderFeedbackEntity orderFeedbackEntity) {
        this.guid = orderFeedbackEntity.getGuid();
        this.orderGuid = orderFeedbackEntity.getOrderGuid();
        this.orderFeedbackAction = orderFeedbackEntity.getOrderFeedbackAction();
        this.serviceQualityRating = orderFeedbackEntity.getServiceQualityRating();
        this.productQualityRating = orderFeedbackEntity.getProductQualityRating();
        this.orderFeedbackContent = orderFeedbackEntity.getOrderFeedbackContent();
    }
}
