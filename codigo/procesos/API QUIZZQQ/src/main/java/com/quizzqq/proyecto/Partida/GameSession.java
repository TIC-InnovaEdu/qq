package com.quizzqq.proyecto.Partida;

import java.util.Arrays;
import java.util.List;

public class GameSession {

    private final String sessionId;
    private final String player1;
    private final String player2;
    private int round = 1;

    public GameSession(String sessionId, String player1, String player2) {
        this.sessionId = sessionId;
        this.player1 = player1;
        this.player2 = player2;
    }

    public List<String> getPlayers() {
        return Arrays.asList(player1, player2);
    }

    public String getOpponent(String playerId) {
        return player1.equals(playerId) ? player2 : player1;
    }

    public void processAnswer(GameMessage message) {
        // Simulación de validación de respuesta
        System.out.println("Jugador " + message.getSenderId() + " respondió: " + message.getContent());
        round++;
    }

    public String getSessionId() {
        return sessionId;
    }
}
