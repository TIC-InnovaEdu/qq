package com.quizzqq.proyecto.Partida;

import com.quizzqq.proyecto.Jwt.JwtService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DARWIN ONOFRE
 */
@RestController
@RequestMapping("/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private final DataSource dataSource;
    private final JwtService jwtService;
    private final GameSessionManager sessionManager;

    @PostMapping
    public ResponseEntity<?> crearPartida(@RequestHeader("Authorization") String token) {
        try (Connection conn = dataSource.getConnection()) {
            String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));

            String sql = "INSERT INTO partidas (id_usuario_anfitrion, id_usuario_invitado) "
                    + "VALUES (?, (SELECT id FROM usuarios WHERE usuario = ?))";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, getUserId(conn, username));
                stmt.setString(2, username);
                stmt.executeUpdate();
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private Integer getUserId(Connection conn, String username) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Usuario no encontrado");
        }
    }

    @PostMapping("/iniciar")
    public String iniciarPartida(@RequestParam String playerId) {
        String sessionId = sessionManager.findOrCreateGame(playerId);
        return sessionId != null ? "Partida creada: " + sessionId : "Esperando oponente...";
    }
}
