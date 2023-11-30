package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TurnResponse {
    private final String msg_NAME = "TurnResponse";
    private String errorMsg= "";
    private boolean wrongTurn;
    private boolean valid;
    private int x;
    private int y;
    private boolean win;
    private boolean lose;
    private boolean draw;
    private boolean gameEnd;
}
