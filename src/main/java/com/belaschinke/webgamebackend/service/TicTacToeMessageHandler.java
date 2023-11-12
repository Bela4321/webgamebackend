package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.service.messageProtocol.InitialResponse;
import com.belaschinke.webgamebackend.service.messageProtocol.MessageSorter;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

public class TicTacToeMessageHandler implements WebSocketHandler {

    @Autowired
    WebgameService webgameService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (!(message.getPayload() instanceof String)) {
            throw new Exception("Invalid message type!");
        }
        String payload = (String) message.getPayload();
        MessageSorter messageSorter = new MessageSorter();
        messageSorter.sortMessage(payload);
        //depending on type, call different methods
        if (messageSorter.getInitialRequest() != null) {
            //call initial request method
            InitialResponse response = webgameService.handleInitialRequest(messageSorter.getInitialRequest());
            //send response
            session.sendMessage(new TextMessage(response.toString()));
        } else if (messageSorter.getTurnRequest() != null) {
            //call turn request method
            TurnResponse response = webgameService.handleTurnRequest(messageSorter.getTurnRequest());
            //send response
            session.sendMessage(new TextMessage(response.toString()));
        } else {
            throw new Exception("Invalid message type!");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
