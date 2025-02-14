package com.quizzqq.proyecto.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.quizzqq.proyecto.Partida.GameWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final GameWebSocketHandler gameWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler, "/game")
                .setAllowedOriginPatterns("*") // Permitir conexiones desde cualquier origen
                .addInterceptors(new HttpSessionHandshakeInterceptor()); // Permitir sesiones HTTP
    }
}
