package com.belaschinke.webgamebackend.config;

import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
public class WebSocketConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WebSocketConnectEventListener.class);

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        // Log WebSocket session instantiation
        logger.info("WebSocket session instantiated: ");
        logger.info(event.getMessage().toString());
        logger.info(event.getMessage().getHeaders().toString());
    }
}
