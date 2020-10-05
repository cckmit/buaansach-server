package vn.com.buaansach.web.guest.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.util.WebSocketMessages;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.shared.service.dto.readwrite.StoreNotificationDTO;
import vn.com.buaansach.web.shared.websocket.dto.DataSocketDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String destination, Object payload) {
        this.messagingTemplate.convertAndSend(destination, payload);
    }

    public void sendCreateOrderNotification(UUID storeGuid, GuestOrderDTO orderDTO) {
        StoreNotificationEntity entity = new StoreNotificationEntity();
        entity.setSeatGuid(orderDTO.getSeatGuid());
        StoreNotificationDTO dto = new StoreNotificationDTO(entity);
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.GUEST_CREATE_ORDER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + storeGuid, socketDTO);
    }

    public void sendUpdateOrderNotification(StoreNotificationDTO dto) {
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.GUEST_UPDATE_ORDER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }

    public void sendPayRequestNotification(StoreNotificationDTO dto) {
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.GUEST_STORE_PAY_REQUEST, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }

    public void sendCallWaiterNotification(StoreNotificationDTO dto) {
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.GUEST_CALL_WAITER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }
}
