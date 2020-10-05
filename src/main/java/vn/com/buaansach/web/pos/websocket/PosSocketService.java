package vn.com.buaansach.web.pos.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.util.WebSocketMessages;
import vn.com.buaansach.web.shared.service.dto.readwrite.StoreNotificationDTO;
import vn.com.buaansach.web.shared.websocket.dto.DataSocketDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String destination, Object payload) {
        this.messagingTemplate.convertAndSend(destination, payload);
    }

    public void sendUpdateOrderNotification(StoreNotificationDTO dto) {
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.POS_UPDATE_ORDER, dto);
        sendMessage(WebSocketEndpoints.TOPIC_POS_PREFIX + dto.getStoreGuid(), socketDTO);
    }

    public void sendOrderReceivedNotification(UUID orderGuid) {
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.POS_RECEIVE_ORDER, null);
        sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + orderGuid, socketDTO);
    }

    public void sendOrderCancelledNotification(UUID orderGuid) {
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.POS_CANCEL_ORDER, null);
        sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + orderGuid, socketDTO);
    }

    public void sendSeatChangeNotification(UUID orderGuid, SeatEntity newSeat) {
        DataSocketDTO socketDTO = new DataSocketDTO(WebSocketMessages.POS_CHANGE_SEAT, newSeat);
        sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + orderGuid, socketDTO);
    }

}
