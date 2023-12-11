package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.service.messageProtocol.InitialResponse;
import com.belaschinke.webgamebackend.service.messageProtocol.MessageSorter;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

public class GameMessageHandler<T extends GameInterface> implements WebSocketHandler {

    @Autowired
    WebgameService<T> webgameService;
    //logger
    private static final Logger logger = LoggerFactory.getLogger(GameMessageHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        //log message
        logger.info("Message received: " + message.getPayload());
        //prep Message
        if (!(message.getPayload() instanceof String)) {
            throw new Exception("Invalid message type!");
        }
        String payload = (String) message.getPayload();
        MessageSorter messageSorter = new MessageSorter();
        messageSorter.sortMessage(payload);

        //depending on type, call different methods
        if (messageSorter.getInitialRequest() != null) {
            //call initial request method
            webgameService.handleInitialRequest(messageSorter.getInitialRequest(), session);
        } else if (messageSorter.getTurnRequest() != null) {
            //call turn request method
            webgameService.handleTurnRequest(messageSorter.getTurnRequest(), session);
        } else {
            throw new Exception("Invalid message type!");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        //todo: remove player from lobby
        webgameService.removePlayerFromLobby(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
