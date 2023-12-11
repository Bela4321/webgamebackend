package com.belaschinke.webgamebackend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;

public class WebSocketLoggingInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketLoggingInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, org.springframework.messaging.MessageChannel channel) {

        logger.info("Message preSend: " + message.getHeaders());
        logger.info("Message preSend: " + message.getPayload());

        return message;
    }


}
