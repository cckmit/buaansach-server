package vn.com.buaansach.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketClientInboundInterceptor());
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
