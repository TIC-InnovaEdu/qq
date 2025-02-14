package com.quizzqq.proyecto.Usuarios;

import com.quizzqq.proyecto.Jwt.JwtService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class UsuariosController {

    private final DataSource dataSource;
    private final JwtService jwtService;

    // Obtener todas las personas
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerPersonas() {
        String sql = "SELECT u.id, u.usuario, u.estado, p.nombres, p.apellidos, p.puntuacion_actual, p.puntuacion_maxima "
                + "FROM usuarios u "
                + "JOIN personas p ON u.id = p.usuario_id";

        return ejecutarConsulta(sql, new ArrayList<>());
    }

    // Obtener personas por lista de IDs
    @CrossOrigin
    @GetMapping("/{ids}")
    public ResponseEntity<List<Map<String, Object>>> obtenerPersonasPorIds(@PathVariable String ids) {
        // Convertir los IDs en una lista
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Construir la consulta con parámetros dinámicos
        String placeholders = idList.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT u.id, u.usuario, u.estado, p.nombres, p.apellidos, p.puntuacion_actual, p.puntuacion_maxima "
                + "FROM usuarios u "
                + "JOIN personas p ON u.id = p.usuario_id "
                + "WHERE u.id IN (" + placeholders + ")";

        return ejecutarConsulta(sql, idList);
    }

    private ResponseEntity<List<Map<String, Object>>> ejecutarConsulta(String sql, List<Integer> parametros) {
        List<Map<String, Object>> resultados = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setInt(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
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
            }

            return ResponseEntity.ok(resultados);

        } catch (SQLException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
