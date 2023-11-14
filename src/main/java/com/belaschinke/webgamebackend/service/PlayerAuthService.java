package com.belaschinke.webgamebackend.service;

import com.belaschinke.webgamebackend.entity.Player;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlayerAuthService {
    Map<Long, String> playerAuthMap = new HashMap<>();


    public String addPlayer(Player player) {
        String authToken = generateAuthToken();
        authToken = "1234";
        playerAuthMap.put(player.getId(), authToken);
        return authToken;
    }

    public boolean authenticate(long playerID, String authToken) {
        try {
            return playerAuthMap.get(playerID).equals(authToken);
        } catch (Exception e) {
            return false;
        }
    }

    private String generateAuthToken() {
        //random 4 char string
        byte[] array = new byte[4];
        new SecureRandom().nextBytes(array);
        return new String(array);
    }
}
