package com.belaschinke.webgamebackend.service.messageProtocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;


@Getter
public class MessageSorter {
    private InitialRequest initialRequest;
    private TurnRequest turnRequest;
    private ObjectMapper objectMapper = new ObjectMapper();


    public void sortMessage(String message) {
        //read json message type
        String messageType = null;
        try {
            messageType = objectMapper.readTree(message).get("msg_NAME").asText();
        } catch (Exception e) {
            System.out.println("Error reading message type");
            return;
        }
        //sort message
        try {
            switch (messageType) {
                case "InitialRequest":
                    initialRequest = objectMapper.readValue(message, InitialRequest.class);
                    break;
                case "TurnRequest":
                    turnRequest = objectMapper.readValue(message, TurnRequest.class);
                    break;
                default:
                    System.out.println("Message type not found");
            }
        } catch (Exception e) {
            System.out.println("Error sorting message");
        }
    }



}
