package com.quizzqq.proyecto.Partida;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameSessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String playerId = session.getId();
        String sessionId = sessionManager.findOrCreateGame(playerId);

        sessions.put(playerId, session);

        if (sessionId != null) {
            GameSession gameSession = sessionManager.getSession(sessionId);
            sendToPlayers(gameSession, "MATCH_FOUND", "Partida iniciada con " + gameSession.getOpponent(playerId));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GameMessage gameMessage = objectMapper.readValue(message.getPayload(), GameMessage.class);
        GameSession gameSession = sessionManager.getSession(gameMessage.getSessionId());

        if (gameSession == null) return;

        if ("ANSWER".equals(gameMessage.getType())) {
            gameSession.processAnswer(gameMessage);
            sendToPlayers(gameSession, "ANSWER_RESULT", "El jugador " + gameMessage.getSenderId() + " respondió.");
        }
    }

    private void sendToPlayers(GameSession gameSession, String type, String content) throws IOException {
        for (String player : gameSession.getPlayers()) {
            WebSocketSession session = sessions.get(player);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new GameMessage(type, gameSession.getSessionId(), "server", content))));
            }
        }
    }
}
