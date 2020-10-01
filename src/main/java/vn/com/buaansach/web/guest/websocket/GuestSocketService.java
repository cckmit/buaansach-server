package vn.com.buaansach.web.guest.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.util.WebSocketMessages;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreNotificationDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.guest.websocket.dto.GuestSocketDTO;

import java.util.UUID;

@Service
public class GuestSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public GuestSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String destination, Object payload) {
        this.messagingTemplate.convertAndSend(destination, payload);
    }

    public void sendCreateOrderNotification(UUID storeGuid, GuestOrderDTO orderDTO) {
        StoreNotificationEntity entity = new StoreNotificationEntity();
        entity.setSeatGuid(orderDTO.getSeatGuid());
        GuestStoreNotificationDTO dto = new GuestStoreNotificationDTO(entity);
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketMessages.GUEST_CREATE_ORDER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + storeGuid, socketDTO);
    }

    public void sendUpdateOrderNotification(GuestStoreNotificationDTO dto) {
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketMessages.GUEST_UPDATE_ORDER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }

    public void sendPayRequestNotification(GuestStoreNotificationDTO dto) {
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketMessages.GUEST_STORE_PAY_REQUEST, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }

    public void sendCallWaiterNotification(GuestStoreNotificationDTO dto) {
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketMessages.GUEST_CALL_WAITER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }
}
