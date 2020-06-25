package vn.com.buaansach.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.com.buaansach.exception.AccessDeniedException;
import vn.com.buaansach.security.util.AuthoritiesConstants;

public class WebSocketClientInboundInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) message.getHeaders().get("simpUser");
            if (!validateSubscription(authenticationToken, headerAccessor.getDestination())) {
                throw new AccessDeniedException("No permission for this topic");
            }
        }
        return message;
    }

    private boolean validateSubscription(AbstractAuthenticationToken authentication, String topicDestination) {
        if (authentication == null || authentication.getAuthorities().isEmpty()) return false;
        if (authentication instanceof AnonymousAuthenticationToken) {
            return !topicDestination.contains("admin") && !topicDestination.contains("pos");
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN))) {
                return !topicDestination.contains("admin");
            }
        }
        return true;
    }
}
