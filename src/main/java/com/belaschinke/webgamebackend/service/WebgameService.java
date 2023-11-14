package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.entity.Player;
import com.belaschinke.webgamebackend.repositories.PlayerRepository;
import com.belaschinke.webgamebackend.service.messageProtocol.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


@Service
public class WebgameService<T extends GameInterface> {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerAuthService playerAuthService;

    @Autowired
    private Lobby<T> lobby;


    public void handleInitialRequest(InitialRequest initialRequest, WebSocketSession session)  {
        Player player = getPlayer(initialRequest.getNickname());
        player.setWebSocketSession(session);
        //check if player exists, else new player

        //is player already active?
        if (lobby.isActive(player.getId())) {
            //send error message
            InitialResponse initialResponse = new InitialResponse();
            initialResponse.setErrorMsg("Player with that nickname already active!");
            initialResponse.setPlayerId(player.getId());
            //send response
            player.sendMessage(initialResponse);
            return ;
        }

        //give player auth token for the session
        String authToken = playerAuthService.addPlayer(player);
        //add player to lobby
        InitialResponseGameStartWrapper initialResponseGameStartWrapper = lobby.addPlayerToRoom(player, initialRequest.getRoomId());
        initialResponseGameStartWrapper.getInitialResponse().setAuthToken(authToken);
        //send response
        player.sendMessage(initialResponseGameStartWrapper.getInitialResponse());
        //send game start responses
        if (initialResponseGameStartWrapper.getGameStartResponse() != null) {
            player.sendMessage(initialResponseGameStartWrapper.getGameStartResponse());
            Player partner = lobby.getPartner(player.getId());
            partner.sendMessage(initialResponseGameStartWrapper.getGameStartResponse());
        }
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

    public void handleTurnRequest(TurnRequest turnRequest, WebSocketSession session) {
        //authenticate player
        if (!playerAuthService.authenticate(turnRequest.getPlayerId(), turnRequest.getAuthToken())) {
            //send error message
            TurnResponse turnResponse = new TurnResponse();
            turnResponse.setErrorMsg("Authentication failed!");
            turnResponse.setValid(false);
            //send response
            try{
                session.sendMessage(new TextMessage(turnResponse.toString()));
            } catch (Exception e) {
                System.out.println("Error sending message to player: " + turnRequest.getPlayerId());
            }
            return ;
        }

        //get Player
        Player player = playerRepository.findById(turnRequest.getPlayerId());
        player.setWebSocketSession(session);

        //get game of player
        T game = lobby.getGame(turnRequest.getPlayerId());
        //nullgame
        if (game == null) {
            //send error message
            TurnResponse turnResponse = new TurnResponse();
            turnResponse.setErrorMsg("No game found!");
            turnResponse.setValid(false);
            player.sendMessage(turnResponse);
            return ;
        }
        //get partner
        Player partner = lobby.getPartner(turnRequest.getPlayerId());
        //nullpartner
        if (partner == null) {
            //send error message
            TurnResponse turnResponse = new TurnResponse();
            turnResponse.setErrorMsg("partner abandoned!");
            turnResponse.setValid(false);
            player.sendMessage(turnResponse);
        }
        TurnResponseUpdateWrapper turnResponseUpdateWrapper = game.handleTurn(turnRequest);
        player.sendMessage(turnResponseUpdateWrapper.getTurnResponse());
        if (turnResponseUpdateWrapper.getGameUpdateResponse() != null) {
            partner.sendMessage(turnResponseUpdateWrapper.getGameUpdateResponse());
        }
    }

    public void removePlayerFromLobby(WebSocketSession session) {
        //find playerId
        long playerId = lobby.getPlayerIdBySession(session);
        if (playerId == -1) {
            System.out.println("Error removing player from lobby: Player not found!");
            return;
        }
        lobby.removePlayerFromRoom(playerId);
    }
}
