
package com.quizzqq.proyecto.Partida;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author DARWIN ONOFRE
 */
@Service
@RequiredArgsConstructor
public class PartidaService {

    private final DataSource dataSource;
//
//    public void procesarPartida(PartidaRequest request) throws SQLException {
//        Connection conn = null;
//        try {
//            conn = dataSource.getConnection();
//            conn.setAutoCommit(false);
//
//            // Insertar partida
//            String sqlPartida = "INSERT INTO partidas (...) VALUES (...)";
//            try (PreparedStatement stmt = conn.prepareStatement(sqlPartida)) {
//                // Configurar parámetros
//                stmt.executeUpdate();
//            }
//
//            // Insertar historial
//            String sqlHistorial = "INSERT INTO historial_partidas (...) VALUES (...)";
//            try (PreparedStatement stmt = conn.prepareStatement(sqlHistorial)) {
//                // Configurar parámetros
//                stmt.executeUpdate();
//            }
//
//            conn.commit();
//        } catch (SQLException e) {
//            if (conn != null) {
//                conn.rollback();
//            }
//            throw e;
//        } finally {
//            if (conn != null) {
//                conn.setAutoCommit(true);
//                conn.close();
//            }
//        }
//    }
}
