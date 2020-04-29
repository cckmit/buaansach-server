package vn.com.buaansach.web.guest.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vn.com.buaansach.web.guest.rest.GuestOrderResource;
import vn.com.buaansach.web.guest.service.GuestOrderService;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCreateOrderDTO;

@Controller
public class GuestOrderSocket implements ApplicationListener<SessionDisconnectEvent> {
    private final String ENTITY_NAME = "guest-order";
    private final Logger log = LoggerFactory.getLogger(GuestOrderResource.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GuestOrderService guestOrderService;

    public GuestOrderSocket(SimpMessagingTemplate simpMessagingTemplate, GuestOrderService guestOrderService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.guestOrderService = guestOrderService;
    }

    @MessageMapping("/make-order/{storeGuid}")
    public void simple(@DestinationVariable String storeGuid, @Payload GuestCreateOrderDTO payload) {
        GuestOrderDTO dto = guestOrderService.createOrder(payload);
        simpMessagingTemplate.convertAndSend("/topic/pos/" + storeGuid, dto);
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

    }
}
