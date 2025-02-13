package com.quizzqq.proyecto.Partida;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class GameSessionManager {

    private final Map<String, GameSession> activeSessions = new ConcurrentHashMap<>();
    private final Queue<String> waitingPlayers = new LinkedList<>();

    public synchronized String findOrCreateGame(String playerId) {
        if (!waitingPlayers.isEmpty()) {
            String opponentId = waitingPlayers.poll();
            String sessionId = UUID.randomUUID().toString();
            GameSession session = new GameSession(sessionId, playerId, opponentId);
            activeSessions.put(sessionId, session);
            return sessionId;
        } else {
            waitingPlayers.add(playerId);
            return null; // Indica que sigue esperando
        }
    }

    public GameSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }
}
