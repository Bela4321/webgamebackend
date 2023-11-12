package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.service.messageProtocol.TurnRequest;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnResponse;

public interface GameInterface {

    TurnResponse handleTurn(TurnRequest turnRequest);
}
