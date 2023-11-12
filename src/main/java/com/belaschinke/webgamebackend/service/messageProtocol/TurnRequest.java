package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TurnRequest {
    private long playerId;
    private String authToken;
    //move
    private int x;
    private int y;


}
