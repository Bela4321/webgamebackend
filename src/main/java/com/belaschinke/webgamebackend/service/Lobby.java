package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.entity.Player;
import com.belaschinke.webgamebackend.service.GameInterface;
import com.belaschinke.webgamebackend.service.messageProtocol.GameStartResponse;
import com.belaschinke.webgamebackend.service.messageProtocol.InitialResponse;
import com.belaschinke.webgamebackend.service.messageProtocol.InitialResponseGameStartWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
public class Lobby<T extends GameInterface> {
    private Map<Long, T> roomGameMap = new HashMap<>();
    private Map<Player, Long> playerRoomMap = new HashMap<>();

    private Class<T> gameType;

    public Lobby(Class<T> gameType) {
        this.gameType = gameType;
    }


    public InitialResponseGameStartWrapper addPlayerToRoom(Player player, long roomId) {
        InitialResponse initialResponse = new InitialResponse();
        initialResponse.setPlayerId(player.getId());
        initialResponse.setRoomId(roomId);
        //how many partners does the room have
        Player[] partners = playerRoomMap.entrySet().stream().filter(entry -> entry.getValue() == roomId).map(Map.Entry::getKey).toArray(Player[]::new);
        if (partners.length >=2) {
            //already full
            initialResponse.setErrorMsg("Room already full!");
            return new InitialResponseGameStartWrapper(initialResponse, null);
        }
        //add player to room
        playerRoomMap.put(player, roomId);
        if (partners.length == 0) {
            initialResponse.setSuccessMsg("Room created!");
            return new InitialResponseGameStartWrapper(initialResponse, null);
        }
        //1 partner-> game can start
        createGame(player, partners[0], roomId);
        initialResponse.setSuccessMsg("Room joined!");

        GameStartResponse gameStartResponse = new GameStartResponse();
        gameStartResponse.setRoomId(roomId);
        gameStartResponse.setPlayer1Id(player.getId());
        gameStartResponse.setPlayer2Id(partners[0].getId());
        gameStartResponse.setPlayer1Nickname(player.getNickname());
        gameStartResponse.setPlayer2Nickname(partners[0].getNickname());
        gameStartResponse.setPlayer1Turn(roomGameMap.get(roomId).getTurn() == 1);

        return new InitialResponseGameStartWrapper(initialResponse, gameStartResponse);
    }

    public boolean isActive(Long playerId) {
        for (Player player : playerRoomMap.keySet()) {
            if (Objects.equals(player.getId(), playerId)) {
                return true;
            }
        }
        return false;
    }

    public void removePlayerFromRoom(long playerId) {
        for (Player player : playerRoomMap.keySet()) {
            if (Objects.equals(player.getId(), playerId)) {
                playerRoomMap.remove(player);
                return;
            }
        }
    }

    public T getGame(long playerId) {
        Long roomId = playerRoomMap.entrySet().stream().filter(entry -> entry.getKey().getId() == playerId).map(Map.Entry::getValue).findFirst().orElse(null);
        if (roomId == null) {
            return null;
        }
        return roomGameMap.get(roomId);
    }

    public void createGame(Player player, Player partner, long roomId) {
        T game = null;
        try {
            game = gameType.getDeclaredConstructor(Player.class, Player.class).newInstance(player, partner);
            roomGameMap.put(roomId, game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getPartner(long playerId) {
        Long roomId = playerRoomMap.entrySet().stream().filter(entry -> entry.getKey().getId() == playerId).map(Map.Entry::getValue).findFirst().orElse(null);
        if (roomId == null) {
            return null;
        }
        Player[] partners = playerRoomMap.entrySet().stream().filter(entry -> entry.getValue() == roomId).map(Map.Entry::getKey).toArray(Player[]::new);
        if (partners.length != 2) {
            return null;
        }
        return partners[0].getId() == playerId ? partners[1] : partners[0];
    }

    public long getPlayerIdBySession(WebSocketSession session) {
        for (Player player : playerRoomMap.keySet()) {
            if (player.getWebSocketSession() == session) {
                return player.getId();
            }
        }
        return -1;
    }
}
