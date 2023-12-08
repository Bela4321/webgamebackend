package com.belaschinke.webgamebackend.config;

import com.belaschinke.webgamebackend.service.GameMessageHandler;
import com.belaschinke.webgamebackend.service.tocTacToe.TicTacToeGame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ticTacToe").setAllowedOrigins("*").withSockJS();
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ticTacToeHandler(), "/ticTacToe")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes( "/app");
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