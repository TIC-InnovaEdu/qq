package com.quizzqq.proyecto.Partida;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMessage {
    private String type;     // MATCH_FOUND, QUESTION, ANSWER
    private String sessionId;
    private String senderId;
    private String content;
}
