package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GameUpdateResponse {
    private final String msg_NAME = "GameUpdateResponse";
    private String errorMsg = "";
    //turn
    private int x;
    private int y;
    private long playerId;
    private int roomId;
    private boolean win;
    private boolean lose;
    private boolean draw;
    private boolean gameEnd;

}
