package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TurnResponse {
    private final String MSG_NAME = "TurnResponse";
    private String ErrorMsg;
    private boolean wrongTurn;
    private boolean valid;
    private boolean win;
    private boolean draw;
    private boolean lose;
}
