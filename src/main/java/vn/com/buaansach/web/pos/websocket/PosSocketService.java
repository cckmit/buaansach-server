package vn.com.buaansach.web.pos.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PosSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public PosSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String destination, Object payload) {
        this.messagingTemplate.convertAndSend(destination, payload);
    }
}
