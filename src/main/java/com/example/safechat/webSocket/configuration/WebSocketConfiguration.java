package com.example.safechat.webSocket.configuration;

import com.example.safechat.webSocket.interceptor.WebSocketAuthChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketAuthChannelInterceptor webSocketAuthChannelInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app").enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(webSocketAuthChannelInterceptor);
    }
}
