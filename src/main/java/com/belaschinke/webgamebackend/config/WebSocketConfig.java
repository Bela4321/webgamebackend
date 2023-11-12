package com.belaschinke.webgamebackend.config;

import com.belaschinke.webgamebackend.service.TicTacToeMessageHandler;
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
    public WebSocketHandler ticTacToeHandler() {
        return new TicTacToeMessageHandler();
    }

}