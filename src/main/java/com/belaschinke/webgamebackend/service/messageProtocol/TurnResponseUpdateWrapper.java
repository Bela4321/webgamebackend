package com.belaschinke.webgamebackend.service.messageProtocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TurnResponseUpdateWrapper {
    private TurnResponse turnResponse;
    private GameUpdateResponse gameUpdateResponse;
}
