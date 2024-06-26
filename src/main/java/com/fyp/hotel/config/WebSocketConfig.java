package com.fyp.hotel.config;

import com.fyp.hotel.filter.WebSocketInterceptor;
import com.fyp.hotel.webSocketImpl.SocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler(), "/chat/{hotelId}")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketInterceptor());
    }

    @Bean
    public WebSocketHandler socketHandler() {
        return new SocketHandler();
    }
}