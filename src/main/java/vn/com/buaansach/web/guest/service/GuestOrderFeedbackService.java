package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderFeedbackEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.GuestErrorCode;
import vn.com.buaansach.web.guest.exception.GuestBadRequestException;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestOrderFeedbackRepository;
import vn.com.buaansach.web.guest.repository.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderFeedbackDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestOrderFeedbackService {
    private final GuestOrderRepository guestOrderRepository;
    private final GuestOrderFeedbackRepository guestOrderFeedbackRepository;
    private final GuestStoreRepository guestStoreRepository;

    public void sendFeedback(GuestOrderFeedbackDTO payload) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(()-> new GuestResourceNotFoundException(GuestErrorCode.ORDER_NOT_FOUND));

        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(()-> new GuestResourceNotFoundException(GuestErrorCode.STORE_NOT_FOUND));

        if (guestOrderFeedbackRepository.findOneByOrderGuid(payload.getOrderGuid()).isPresent())
            throw new GuestBadRequestException(GuestErrorCode.ORDER_FEEDBACK_EXIST);

        OrderFeedbackEntity orderFeedbackEntity = new OrderFeedbackEntity();
        orderFeedbackEntity.setGuid(UUID.randomUUID());
        orderFeedbackEntity.setOrderGuid(payload.getOrderGuid());
        orderFeedbackEntity.setStoreGuid(storeEntity.getGuid());
        orderFeedbackEntity.setOrderFeedbackAction(payload.getOrderFeedbackAction());
        orderFeedbackEntity.setProductQualityRating(payload.getProductQualityRating());
        orderFeedbackEntity.setServiceQualityRating(payload.getServiceQualityRating());
        orderFeedbackEntity.setFeedbackContent(payload.getFeedbackContent());
        guestOrderFeedbackRepository.save(orderFeedbackEntity);
    }

    public GuestOrderFeedbackDTO getFeedback(String orderGuid) {
        OrderFeedbackEntity orderFeedbackEntity = guestOrderFeedbackRepository.findOneByOrderGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException(GuestErrorCode.ORDER_FEEDBACK_NOT_FOUND));
        return new GuestOrderFeedbackDTO(orderFeedbackEntity);
    }
}
