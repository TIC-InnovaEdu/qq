package com.quizzqq.proyecto.Partida;

import java.util.Arrays;
import java.util.List;

public class GameSession {

    private final String sessionId;
    private final String player1;
    private final String player2;
    private int round = 0;
    private int player1Correct = 0;
    private int player1Incorrect = 0;
    private int player2Correct = 0;
    private int player2Incorrect = 0;

    // Datos de la pregunta actual
    private Integer currentQuestionId;
    private String correctAnswer;

    public GameSession(String sessionId, String player1, String player2) {
        this.sessionId = sessionId;
        this.player1 = player1;
        this.player2 = player2;
    }

    public List<String> getPlayers() {
        return Arrays.asList(player1, player2);
    }

    // Devuelve el oponente del jugador indicado
    public String getOpponent(String playerId) {
        return player1.equals(playerId) ? player2 : player1;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getRound() {
        return round;
    }

    public void incrementRound() {
        round++;
    }

    // Guarda la pregunta actual y la respuesta correcta
    public void setCurrentQuestion(Integer questionId, String correctAnswer) {
        this.currentQuestionId = questionId;
        this.correctAnswer = correctAnswer;
    }

    public Integer getCurrentQuestionId() {
        return currentQuestionId;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // Actualiza las estadísticas según el jugador y si respondió bien
    public void updateStats(String senderId, boolean correct) {
        if (player1.equals(senderId)) {
            if (correct) {
                player1Correct++;
            } else {
                player1Incorrect++;
            }
        } else if (player2.equals(senderId)) {
            if (correct) {
                player2Correct++;
            } else {
                player2Incorrect++;
            }
        }
    }

    // Devuelve un resumen para cada jugador
    public String getSummaryForPlayer(String playerId) {
        if (player1.equals(playerId)) {
            return "Jugador " + playerId + " respondió " + round + " preguntas: "
                    + player1Correct + " correctas y " + player1Incorrect + " incorrectas.";
        } else if (player2.equals(playerId)) {
            return "Jugador " + playerId + " respondió " + round + " preguntas: "
                    + player2Correct + " correctas y " + player2Incorrect + " incorrectas.";
        }
        return "";
    }
}
