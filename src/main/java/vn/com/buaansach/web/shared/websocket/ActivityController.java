package vn.com.buaansach.web.shared.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.web.shared.websocket.dto.ActivityDTO;
import vn.com.buaansach.web.shared.websocket.enumeration.ActiveStatus;

import java.security.Principal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static vn.com.buaansach.config.WebSocketConfig.IP_ADDRESS;

@Controller
@RequiredArgsConstructor
public class ActivityController implements ApplicationListener<SessionDisconnectEvent> {
    static Set<ActivityDTO> activeUsers = Collections.synchronizedSet(new HashSet<>());
    private final SimpMessageSendingOperations messagingTemplate;

    /* prefix /app */
    @MessageMapping(WebSocketEndpoints.APP_ACTIVITY)
    @SendTo(WebSocketEndpoints.TOPIC_ADMIN_TRACKER)
    public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        activityDTO.setStatus(ActiveStatus.CONNECTED);
        activityDTO.setUserLogin(principal.getName());
        activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
        String ipAddress = stompHeaderAccessor.getSessionAttributes() != null ? stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString() : "";
        activityDTO.setIpAddress(ipAddress);
        activityDTO.setTime(Instant.now());
        activeUsers.add(activityDTO);
        return activityDTO;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setStatus(ActiveStatus.DISCONNECTED);
        activityDTO.setSessionId(event.getSessionId());
        String userLogin = event.getUser() != null ? event.getUser().getName() : Constants.ANONYMOUS_USER;
        activityDTO.setUserLogin(userLogin);
        activeUsers.remove(activityDTO);
        messagingTemplate.convertAndSend(WebSocketEndpoints.TOPIC_ADMIN_TRACKER, activityDTO);
    }
}
