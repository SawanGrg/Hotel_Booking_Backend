package com.fyp.hotel.webSocketImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class SocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<WebSocketSession> sessions = new HashSet<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //websocket session means a connection between client and server
        log.info("Connection established on session: {}", session.getId());
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String clientMessage = (String) message.getPayload();
        log.info("Received message: {} from session: {}", clientMessage, session.getId());
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(clientMessage));
        }
    }

    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        byte[] imageData = message.getPayload().array();
        log.info("Received binary message from session: {}", session.getId());
        // Handle binary message
        // Process the binary data as per your application requirements
        // For example, save the image to storage, process it, or broadcast it to other clients.
        // Your implementation here...
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Error on session: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}