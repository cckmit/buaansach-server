package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderFeedbackEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosOrderFeedbackRepository;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderFeedbackDTO;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosOrderFeedbackService {
    private final PosOrderRepository posOrderRepository;
    private final PosOrderFeedbackRepository posOrderFeedbackRepository;
    private final PosStoreRepository posStoreRepository;
    private final PosStoreSecurity posStoreSecurity;

    public void sendFeedback(PosOrderFeedbackDTO payload) {
        posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFound@" + payload.getStoreGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderFeedbackEntity orderFeedbackEntity = new OrderFeedbackEntity();
        orderFeedbackEntity.setGuid(UUID.randomUUID());
        orderFeedbackEntity.setOrderGuid(payload.getOrderGuid());
        orderFeedbackEntity.setStoreGuid(storeEntity.getGuid());
        orderFeedbackEntity.setOrderFeedbackAction(payload.getOrderFeedbackAction());
        orderFeedbackEntity.setProductQualityRating(payload.getProductQualityRating());
        orderFeedbackEntity.setServiceQualityRating(payload.getServiceQualityRating());
        orderFeedbackEntity.setFeedbackContent(payload.getFeedbackContent());
        posOrderFeedbackRepository.save(orderFeedbackEntity);
    }

    public PosOrderFeedbackDTO getFeedback(String orderGuid) {
        Optional<OrderFeedbackEntity> optional = posOrderFeedbackRepository.findOneByOrderGuid(UUID.fromString(orderGuid));
        return optional.map(PosOrderFeedbackDTO::new).orElseGet(() -> new PosOrderFeedbackDTO(new OrderFeedbackEntity()));
    }
}
