package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InitialResponseGameStartWrapper {
    InitialResponse initialResponse;
    GameStartResponse gameStartResponse;
}
