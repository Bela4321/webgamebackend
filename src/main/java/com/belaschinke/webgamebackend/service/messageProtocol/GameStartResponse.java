package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class GameStartResponse {
    private int roomId;

    //game-specific Data
    private long player1Id;
    private long player2Id;
    private String player1Nickname;
    private String player2Nickname;
    private boolean player1Turn;

}
