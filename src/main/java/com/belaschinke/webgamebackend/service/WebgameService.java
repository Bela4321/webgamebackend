package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.entity.Player;
import com.belaschinke.webgamebackend.repositories.PlayerRepository;
import com.belaschinke.webgamebackend.service.messageProtocol.InitialRequest;
import com.belaschinke.webgamebackend.service.messageProtocol.InitialResponse;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnRequest;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnResponse;
import com.belaschinke.webgamebackend.service.tocTacToe.Lobby;
import com.belaschinke.webgamebackend.service.tocTacToe.TicTacToeGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WebgameService<T extends GameInterface> {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerAuthService playerAuthService;

    @Autowired
    private Lobby<T> lobby;


    public InitialResponse handleInitialRequest(InitialRequest initialRequest) {
        Player player = getPlayer(initialRequest.getNickname());
        //check if player exists, else new player

        //is player already active?
        if (lobby.isActive(player.getId())) {
            //send error message
            InitialResponse initialResponse = new InitialResponse();
            initialResponse.setErrorMsg("Player with that nickname already active!");
            initialResponse.setPlayerId(player.getId());
            return initialResponse;
        }

        //give player auth token for the session
        String authToken = playerAuthService.addPlayer(player);
        //add player to lobby
        InitialResponse initialResponse = lobby.addPlayerToRoom(player.getId(), initialRequest.getRoomId());
        initialResponse.setAuthToken(authToken);
        return initialResponse;
    }

    private Player getPlayer(String nickname) {
        Player player = playerRepository.findByNickname(nickname);
        if (player == null) {
            player = new Player(nickname);
            playerRepository.save(player);
            //todo: is neccessary for id?
            player = playerRepository.findByNickname(nickname);
        }
        return player;
    }

    public TurnResponse handleTurnRequest(TurnRequest turnRequest) {
        //authenticate player
        if (!playerAuthService.authenticate(turnRequest.getPlayerId(), turnRequest.getAuthToken())) {
            //send error message
            TurnResponse turnResponse = new TurnResponse();
            turnResponse.setErrorMsg("Authentication failed!");
            turnResponse.setValid(false);
            return turnResponse;
        }
        //get game of player
        T game = lobby.getGame(turnRequest.getPlayerId());
        //nullgame
        if (game == null) {
            //send error message
            TurnResponse turnResponse = new TurnResponse();
            turnResponse.setErrorMsg("No game found!");
            turnResponse.setValid(false);
            return turnResponse;
        }
        TurnResponse turnResponse = game.handleTurn(turnRequest);

        return turnResponse;
    }
}
