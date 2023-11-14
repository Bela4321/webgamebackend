package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.service.messageProtocol.TurnRequest;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnResponseUpdateWrapper;

public interface GameInterface {

    TurnResponseUpdateWrapper handleTurn(TurnRequest turnRequest);

    public int getTurn();
}
