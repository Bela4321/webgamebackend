package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InitialResponse {
    private final String MSG_NAME = "InitialResponse";
    private String ErrorMsg;
    private String SuccessMsg;
    private String authToken;
    private long playerId;
    private long roomId;
}
