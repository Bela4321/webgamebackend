package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InitialResponse {
    private final String msg_NAME = "InitialResponse";
    private String errorMsg= "";
    private String successMsg;
    private String authToken;
    private long playerId;
    private long roomId;
}
