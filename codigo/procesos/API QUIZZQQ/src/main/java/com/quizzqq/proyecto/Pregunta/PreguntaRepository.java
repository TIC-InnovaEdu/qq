
package com.quizzqq.proyecto.Pregunta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DARWIN ONOFRE
 */
@Repository
public class PreguntaRepository {

    private final JdbcTemplate jdbcTemplate;

    public PreguntaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class PreguntaRowMapper implements RowMapper<Pregunta> {
        @Override
        public Pregunta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Pregunta.builder()
                .id(rs.getInt("id"))
                .codigoTema(rs.getString("id_codigo_catalogo_tema"))
                .textoPregunta(rs.getString("texto_pregunta"))
                .estado(rs.getString("estado"))
                .build();
        }
    }

    public List<Pregunta> findByTema(String codigoTema) {
        String sql = "SELECT * FROM preguntas WHERE id_codigo_catalogo_tema = ? AND estado = 'A'";
        return jdbcTemplate.query(sql, new PreguntaRowMapper(), codigoTema);
    }
}
