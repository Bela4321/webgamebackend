package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InitialRequest {
    private final String MSG_NAME = "InitialRequest";

    private String nickname;
    private long roomId;
}
