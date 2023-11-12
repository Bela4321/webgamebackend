package com.belaschinke.webgamebackend.service.tocTacToe;

import com.belaschinke.webgamebackend.service.GameInterface;
import com.belaschinke.webgamebackend.service.messageProtocol.InitialResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Lobby<T extends GameInterface> {
    private Map<Long, T> roomGameMap = new HashMap<>();
    private Map<Long, Long> playerRoomMap = new HashMap<>();

    private Class<T> gameType;

    public Lobby(Class<T> gameType) {
        this.gameType = gameType;
    }


    public InitialResponse addPlayerToRoom(long playerId, long roomId) {
        InitialResponse initialResponse = new InitialResponse();
        initialResponse.setPlayerId(playerId);
        initialResponse.setRoomId(roomId);
        //how many partners does the room have
        Long[] partners = playerRoomMap.entrySet().stream().filter(entry -> entry.getValue() == roomId).map(Map.Entry::getKey).toArray(Long[]::new);
        if (partners.length >=2) {
            //already full
            initialResponse.setErrorMsg("Room already full!");
            return initialResponse;
        }
        //add player to room
        playerRoomMap.put(playerId, roomId);
        if (partners.length == 0) {
            initialResponse.setSuccessMsg("Room created!");
            return initialResponse;
        }
        //1 partner-> game can start
        createGame(playerId, partners[0], roomId);
        initialResponse.setSuccessMsg("Room joined!");
        return initialResponse;
    }

    public boolean isActive(Long playerId) {
        return playerRoomMap.containsKey(playerId);
    }

    public void removePlayerFromRoom(long playerId) {
        playerRoomMap.remove(playerId);
    }

    public T getGame(long playerId) {
        Long roomId = playerRoomMap.get(playerId);
        if (roomId == null) {
            //todo: error handling
            return null;
        }
        return roomGameMap.get(roomId);
    }

    public void createGame(long playerId, long partnerId, long roomId) {
        T game = null;
        try {
            game = gameType.getDeclaredConstructor(long.class, long.class).newInstance(playerId, partnerId);
            roomGameMap.put(roomId, game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
