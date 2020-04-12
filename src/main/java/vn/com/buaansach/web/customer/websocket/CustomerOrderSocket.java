package vn.com.buaansach.web.customer.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class CustomerOrderSocket implements ApplicationListener<SessionDisconnectEvent> {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public CustomerOrderSocket(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/make-order/{storeGuid}/{seatGuid}")
    public void simple(@DestinationVariable String storeGuid, @DestinationVariable String seatGuid, @Payload String test) {
        simpMessagingTemplate.convertAndSend("/topic/pos/" + storeGuid, test);
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

    }
}
