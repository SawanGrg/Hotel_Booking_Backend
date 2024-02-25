package com.fyp.hotel.filter;

import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

public class WebSocketInterceptor implements HandshakeInterceptor {

    //this handshake means the connection between client and server
    //known as WebSocket handshake
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, java.util.Map<String, Object> attributes) throws Exception {
        // Perform tasks before the WebSocket handshake, such as authentication
        System.out.println("Before Handshake");
        return true; // Return true to proceed with the handshake, or false to abort it
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // Perform tasks after the WebSocket handshake
        System.out.println("After Handshake");
    }
}
