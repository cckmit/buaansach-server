package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderFeedbackEntity;
import vn.com.buaansach.web.guest.repository.GuestOrderFeedbackRepository;
import vn.com.buaansach.web.guest.service.dto.write.GuestOrderFeedbackDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestOrderFeedbackService {
    private final GuestOrderFeedbackRepository guestOrderFeedbackRepository;

    public void sendFeedback(GuestOrderFeedbackDTO payload) {
        OrderFeedbackEntity orderFeedbackEntity = new OrderFeedbackEntity();
        orderFeedbackEntity.setGuid(UUID.randomUUID());
        orderFeedbackEntity.setOrderGuid(payload.getOrderGuid());
        orderFeedbackEntity.setOrderFeedbackAction(payload.getOrderFeedbackAction());
        orderFeedbackEntity.setProductQualityRating(payload.getProductQualityRating());
        orderFeedbackEntity.setServiceQualityRating(payload.getServiceQualityRating());
        orderFeedbackEntity.setFeedbackContent(payload.getFeedbackContent());
        guestOrderFeedbackRepository.save(orderFeedbackEntity);
    }
}
