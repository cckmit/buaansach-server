package vn.com.buaansach.web.pos.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.util.WebSocketMessages;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreNotificationDTO;
import vn.com.buaansach.web.pos.websocket.dto.PosSocketDTO;

import java.util.UUID;

@Service
public class PosSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public PosSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String destination, Object payload) {
        this.messagingTemplate.convertAndSend(destination, payload);
    }

    public void sendUpdateOrderNotification(PosStoreNotificationDTO dto) {
        PosSocketDTO socketDTO = new PosSocketDTO(WebSocketMessages.POS_UPDATE_ORDER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }

    public void sendOrderReceivedNotification(UUID orderGuid) {
        PosSocketDTO socketDTO = new PosSocketDTO(WebSocketMessages.POS_RECEIVE_ORDER, null);
        sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + orderGuid, socketDTO);
    }

    public void sendOrderCancelledNotification(UUID orderGuid) {
        PosSocketDTO socketDTO = new PosSocketDTO(WebSocketMessages.POS_CANCEL_ORDER, null);
        sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + orderGuid, socketDTO);
    }

    public void sendSeatChangeNotification(UUID orderGuid, SeatEntity newSeat) {
        PosSocketDTO socketDTO = new PosSocketDTO(WebSocketMessages.POS_CHANGE_SEAT, newSeat);
        sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + orderGuid, socketDTO);
    }

}
