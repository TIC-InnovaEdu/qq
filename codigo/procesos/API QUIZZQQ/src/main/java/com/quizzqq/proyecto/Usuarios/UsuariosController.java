package com.quizzqq.proyecto.Usuarios;

import com.quizzqq.proyecto.Jwt.JwtService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DARWIN ONOFRE
 */
@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class UsuariosController {

    private final DataSource dataSource;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerPersonas() {
        String sql = "SELECT u.id, u.usuario, u.estado, p.nombres, p.apellidos, p.puntuacion_actual, p.puntuacion_maxima "
                + "FROM usuarios u "
                + "JOIN personas p ON u.id = p.usuario_id";

        List<Map<String, Object>> resultados = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> persona = new HashMap<>();
                persona.put("id", rs.getInt("id"));
                persona.put("usuario", rs.getString("usuario"));
                persona.put("estado", rs.getString("estado"));
                persona.put("nombres", rs.getString("nombres"));
                persona.put("apellidos", rs.getString("apellidos"));
                persona.put("puntuacion_actual", rs.getInt("puntuacion_actual"));
                persona.put("puntuacion_maxima", rs.getInt("puntuacion_maxima"));
                resultados.add(persona);
            }

            return ResponseEntity.ok(resultados);

        } catch (SQLException e) {
            return ResponseEntity.status(500).build();
        }
    }

    /*
    // Versión con seguridad JWT
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerPersonas(@RequestHeader("Authorization") String token) {
        try {
            // Validar token
            String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
            if (!jwtService.isTokenValid(token, userDetailsService.loadUserByUsername(username))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Resto del código igual...
            return ResponseEntity.ok(resultados);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
     */
}
