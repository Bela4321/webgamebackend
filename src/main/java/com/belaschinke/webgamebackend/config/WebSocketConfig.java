package com.belaschinke.webgamebackend.config;

import com.belaschinke.webgamebackend.service.GameMessageHandler;
import com.belaschinke.webgamebackend.service.tocTacToe.Lobby;
import com.belaschinke.webgamebackend.service.tocTacToe.TicTacToeGame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ticTacToeHandler(), "/ticTacToe");
    }

    @Bean
    public GameMessageHandler<TicTacToeGame> ticTacToeHandler() {
        return new GameMessageHandler();
    }

    @Bean
    public Class<TicTacToeGame> ticTacToeGameClass() {
        return TicTacToeGame.class;
    }
}