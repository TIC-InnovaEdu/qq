package com.quizzqq.proyecto.Partida;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizzqq.proyecto.Pregunta.Pregunta;
import com.quizzqq.proyecto.Pregunta.PreguntaRepository;
import com.quizzqq.proyecto.Pregunta.Respuesta;
import com.quizzqq.proyecto.Pregunta.RespuestaRepository;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameSessionManager sessionManager;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    // Mapa de sesiones WebSocket: clave = session.getId(), valor = WebSocketSession
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // Lista simulada de temas disponibles (en producción, consulta la tabla de catalogos)
    private final List<String> availableTemas = List.of("HISTORIA01", "CIENCIA01", "DEPORTES01");

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String playerId = session.getId();
        System.out.println("Nuevo jugador conectado: " + playerId);
        sessions.put(playerId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GameMessage gameMessage = objectMapper.readValue(message.getPayload(), GameMessage.class);
        System.out.println("Mensaje recibido: " + gameMessage);

        if ("FIND_MATCH".equals(gameMessage.getType())) {
            // Usamos el id único de la sesión para emparejar
            String playerId = session.getId();
            System.out.println("Emparejando jugador con session id: " + playerId);
            String sessionId = sessionManager.findOrCreateGame(playerId);
            if (sessionId == null) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                        new GameMessage("WAITING", null, "server", "Esperando oponente")
                )));
            } else {
                GameSession gameSession = sessionManager.getSession(sessionId);
                if (gameSession != null) {
                    sendToPlayers(gameSession, "MATCH_FOUND", "Partida iniciada entre "
                            + gameSession.getPlayer1() + " y " + gameSession.getPlayer2());
                    // Envía la primera pregunta de forma aleatoria
                    sendQuestionToPlayers(gameSession);
                    System.out.println("Partida encontrada para " + playerId);
                }
            }
            return;
        }

        if ("ANSWER".equals(gameMessage.getType())) {
            if (gameMessage.getSessionId() == null) {
                System.out.println("Error: gameMessage.sessionId es null.");
                return;
            }
            GameSession gameSession = sessionManager.getSession(gameMessage.getSessionId());
            if (gameSession == null) {
                System.out.println("Error: No se encontró la sesión para gameMessage.");
                return;
            }
            String providedAnswer = gameMessage.getContent();
            String correct = gameSession.getCorrectAnswer();
            boolean isCorrect = providedAnswer != null && providedAnswer.equalsIgnoreCase(correct);
            // Actualiza estadísticas
            gameSession.updateStats(gameMessage.getSenderId(), isCorrect);
            // Envía notificaciones a ambos jugadores
            if (isCorrect) {
                sendToPlayer(gameSession, gameMessage.getSenderId(), new GameMessage("ANSWER_RESULT", gameSession.getSessionId(), "server", "Correcto"));
                String opponent = gameSession.getOpponent(gameMessage.getSenderId());
                sendToPlayer(gameSession, opponent, new GameMessage("OPPONENT_ANSWER", gameSession.getSessionId(), "server", "Tu oponente respondió correctamente"));
            } else {
                sendToPlayer(gameSession, gameMessage.getSenderId(), new GameMessage("ANSWER_RESULT", gameSession.getSessionId(), "server", "Incorrecto"));
                String opponent = gameSession.getOpponent(gameMessage.getSenderId());
                sendToPlayer(gameSession, opponent, new GameMessage("OPPONENT_ANSWER", gameSession.getSessionId(), "server", "Tu oponente respondió incorrectamente"));
            }
            // Incrementa ronda
            gameSession.incrementRound();
            if (gameSession.getRound() < 5) {
                // Envía la siguiente pregunta
                sendQuestionToPlayers(gameSession);
            } else {
                // Final de partida: envía resumen a ambos jugadores
                for (String player : gameSession.getPlayers()) {
                    String summary = gameSession.getSummaryForPlayer(player);
                    sendToPlayer(gameSession, player, new GameMessage("FINAL_SUMMARY", gameSession.getSessionId(), "server", summary));
                }
                // Aquí podrías limpiar la sesión si lo requieres
            }
        }
    }

    private void sendToPlayers(GameSession gameSession, String type, String content) throws IOException {
        for (String player : gameSession.getPlayers()) {
            WebSocketSession wsSession = sessions.get(player);
            if (wsSession != null && wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                        new GameMessage(type, gameSession.getSessionId(), "server", content)
                )));
                System.out.println("Mensaje enviado a " + player + ": " + content);
            }
        }
    }

    private void sendToPlayer(GameSession gameSession, String playerId, GameMessage message) throws IOException {
        WebSocketSession wsSession = sessions.get(playerId);
        if (wsSession != null && wsSession.isOpen()) {
            wsSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            System.out.println("Mensaje enviado a " + playerId + ": " + message.getContent());
        }
    }

    // Selecciona aleatoriamente un tema, luego una pregunta y sus respuestas (1 correcta, 3 incorrectas)
    private void sendQuestionToPlayers(GameSession gameSession) throws IOException {
        // Selecciona un tema aleatorio de los disponibles
        String tema = availableTemas.get(new Random().nextInt(availableTemas.size()));
        List<Pregunta> preguntas = preguntaRepository.findByTema(tema);
        if (preguntas.isEmpty()) {
            System.out.println("No hay preguntas para el tema " + tema);
            return;
        }
        // Selecciona una pregunta aleatoriamente
        Pregunta selected = preguntas.get(new Random().nextInt(preguntas.size()));
        // Obtén las respuestas para la pregunta
        List<Respuesta> respuestasList = respuestaRepository.findByPreguntaId(selected.getId());
        // Asegúrate de tener 4 respuestas (1 correcta y 3 incorrectas)
        if (respuestasList.size() < 4) {
            System.out.println("La pregunta " + selected.getId() + " no tiene suficientes respuestas.");
            return;
        }
        List<String> answers = respuestasList.stream()
                .map(Respuesta::getTextoRespuesta)
                .collect(Collectors.toList());
        // Busca la respuesta correcta (tipo 'C')
        String correctAnswer = respuestasList.stream()
                .filter(r -> "C".equalsIgnoreCase(r.getTipoRespuesta()))
                .findFirst()
                .map(Respuesta::getTextoRespuesta)
                .orElse("");
        // Actualiza la sesión con la pregunta actual
        gameSession.setCurrentQuestion(selected.getId(), correctAnswer);

        // Construye el mensaje de pregunta
        Map<String, Object> questionMsg = new HashMap<>();
        questionMsg.put("type", "QUESTION");
        questionMsg.put("sessionId", gameSession.getSessionId());
        questionMsg.put("question", selected.getTextoPregunta());
        questionMsg.put("answers", answers);
        // Envía el mensaje a cada jugador
        for (String player : gameSession.getPlayers()) {
            WebSocketSession wsSession = sessions.get(player);
            if (wsSession != null && wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(questionMsg)));
                System.out.println("Pregunta enviada a " + player + ": " + selected.getTextoPregunta());
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try (session) {
            System.out.println("Error en WebSocket para jugador " + session.getId() + ": " + exception.getMessage());
            sessions.remove(session.getId());
        }
    }
}
