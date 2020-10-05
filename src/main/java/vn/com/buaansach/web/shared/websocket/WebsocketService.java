package vn.com.buaansach.web.shared.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.util.WebSocketMessages;
import vn.com.buaansach.web.shared.service.dto.readwrite.StoreNotificationDTO;
import vn.com.buaansach.web.shared.websocket.dto.DataSocketDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebsocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String destination, Object payload) {
        this.messagingTemplate.convertAndSend(destination, payload);
    }

    public void sendUsePointNotification(UUID storeGuid, OrderEntity orderEntity) {
        StoreNotificationEntity entity = new StoreNotificationEntity();
        entity.setSeatGuid(orderEntity.getSeatGuid());
        entity.setOrderGuid(orderEntity.getGuid());
        StoreNotificationDTO dto = new StoreNotificationDTO(entity);
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.GUEST_UPDATE_POINT, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + storeGuid, socketDTO);
    }

}
