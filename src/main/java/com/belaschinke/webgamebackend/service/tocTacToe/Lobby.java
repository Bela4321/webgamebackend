package com.belaschinke.webgamebackend.service.tocTacToe;

import com.belaschinke.webgamebackend.entity.Player;
import com.belaschinke.webgamebackend.service.GameInterface;
import com.belaschinke.webgamebackend.service.messageProtocol.InitialResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class Lobby<T extends GameInterface> {
    private Map<Long, T> roomGameMap = new HashMap<>();
    private Map<Player, Long> playerRoomMap = new HashMap<>();

    private Class<T> gameType;

    public Lobby(Class<T> gameType) {
        this.gameType = gameType;
    }


    public InitialResponse addPlayerToRoom(Player player, long roomId) {
        InitialResponse initialResponse = new InitialResponse();
        initialResponse.setPlayerId(player.getId());
        initialResponse.setRoomId(roomId);
        //how many partners does the room have
        Player[] partners = playerRoomMap.entrySet().stream().filter(entry -> entry.getValue() == roomId).map(Map.Entry::getKey).toArray(Player[]::new);
        if (partners.length >=2) {
            //already full
            initialResponse.setErrorMsg("Room already full!");
            return initialResponse;
        }
        //add player to room
        playerRoomMap.put(player, roomId);
        if (partners.length == 0) {
            initialResponse.setSuccessMsg("Room created!");
            return initialResponse;
        }
        //1 partner-> game can start
        createGame(player, partners[0], roomId);
        initialResponse.setSuccessMsg("Room joined!");
        return initialResponse;
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
        Long roomId = playerRoomMap.get(playerId);
        if (roomId == null) {
            //todo: error handling
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
}
