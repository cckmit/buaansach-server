package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderFeedbackAction;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderFeedbackEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.guest.repository.order.GuestOrderFeedbackRepository;
import vn.com.buaansach.web.guest.repository.order.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderFeedbackDTO;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestOrderFeedbackService {
    private final GuestOrderRepository guestOrderRepository;
    private final GuestOrderFeedbackRepository guestOrderFeedbackRepository;
    private final GuestStoreRepository guestStoreRepository;

    public void sendFeedback(GuestOrderFeedbackDTO payload) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        OrderFeedbackEntity orderFeedbackEntity;
        Optional<OrderFeedbackEntity> optional = guestOrderFeedbackRepository.findOneByOrderGuid(payload.getOrderGuid());
        if (optional.isPresent()) {
            orderFeedbackEntity = optional.get();
        } else {
            orderFeedbackEntity = new OrderFeedbackEntity();
            orderFeedbackEntity.setGuid(UUID.randomUUID());
            orderFeedbackEntity.setOrderGuid(payload.getOrderGuid());
            orderFeedbackEntity.setStoreGuid(storeEntity.getGuid());
            orderFeedbackEntity.setOrderFeedbackAction(OrderFeedbackAction.SUBMIT);
        }
        orderFeedbackEntity.setProductQualityRating(payload.getProductQualityRating());
        orderFeedbackEntity.setServiceQualityRating(payload.getServiceQualityRating());
        orderFeedbackEntity.setOrderFeedbackContent(payload.getOrderFeedbackContent());
        guestOrderFeedbackRepository.save(orderFeedbackEntity);
    }

    public GuestOrderFeedbackDTO getFeedback(String orderGuid) {
        OrderFeedbackEntity orderFeedbackEntity = guestOrderFeedbackRepository.findOneByOrderGuid(UUID.fromString(orderGuid))
                .orElse(new OrderFeedbackEntity());
        return new GuestOrderFeedbackDTO(orderFeedbackEntity);
    }
}
