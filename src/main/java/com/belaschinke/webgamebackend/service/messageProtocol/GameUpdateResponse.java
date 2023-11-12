package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GameUpdateResponse {
    private String ErrorMsg;
    //turn
    private int x;
    private int y;
    private long playerId;
    private int roomId;
    private boolean player1Win;
    private boolean player2Win;
    private boolean draw;
    private boolean gameEnd;

}
