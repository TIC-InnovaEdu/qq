package com.quizzqq.proyecto.Partida;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GameSessionManager {

    // Cola de jugadores en espera
    private final Queue<String> waitingPlayers = new ConcurrentLinkedQueue<>();
    // Mapa de sesiones activas (clave: sessionId, valor: GameSession)
    private final Map<String, GameSession> activeSessions = new ConcurrentHashMap<>();

    /**
     * Si ya hay un jugador en espera (diferente al que solicita), se crea una sesión.
     * De lo contrario, se agrega al jugador en espera y se retorna null.
     */
    public synchronized String findOrCreateGame(String playerId) {
        // Si hay un jugador en espera y no es el mismo
        if (!waitingPlayers.isEmpty() && !waitingPlayers.peek().equals(playerId)) {
            String opponentId = waitingPlayers.poll();
            String sessionId = UUID.randomUUID().toString();
            GameSession session = new GameSession(sessionId, opponentId, playerId);
            activeSessions.put(sessionId, session);
            return sessionId;
        } else {
            // Agrega al jugador en espera
            waitingPlayers.add(playerId);
            return null;
        }
    }

    public GameSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    /**
     * Remueve un jugador de la cola y, si aparece en alguna sesión activa, la elimina.
     */
    public synchronized void removePlayer(String playerId) {
        waitingPlayers.remove(playerId);
        activeSessions.entrySet().removeIf(entry -> entry.getValue().getPlayers().contains(playerId));
    }
}
