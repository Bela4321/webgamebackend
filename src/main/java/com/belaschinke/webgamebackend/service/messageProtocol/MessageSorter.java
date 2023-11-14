package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Getter;
import org.springframework.stereotype.Component;


@Getter
public class MessageSorter {

    private GameStartResponse gameStartResponse;
    private GameUpdateResponse gameUpdateResponse;
    private InitialRequest initialRequest;
    private InitialResponse initialResponse;
    private TurnRequest turnRequest;
    private TurnResponse turnResponse;


    public void sortMessage(String message) {
        String[] messageArray = message.split(":");
        String messageType = messageArray[0];
        switch (messageType) {
            case "GameStartResponse":
                gameStartResponse = MessageParser.parseMessage(messageArray[1], GameStartResponse.class);
                break;
            case "GameUpdateResponse":
                gameUpdateResponse = MessageParser.parseMessage(messageArray[1], GameUpdateResponse.class);
                break;
            case "InitialRequest":
                initialRequest = MessageParser.parseMessage(messageArray[1], InitialRequest.class);
                break;
            case "InitialResponse":
                initialResponse = MessageParser.parseMessage(messageArray[1], InitialResponse.class);
                break;
            case "TurnRequest":
                turnRequest = MessageParser.parseMessage(messageArray[1], TurnRequest.class);
                break;
            case "TurnResponse":
                turnResponse = MessageParser.parseMessage(messageArray[1], TurnResponse.class);
                break;
            default:
                System.out.println("Message type not found");

        }
    }



}
