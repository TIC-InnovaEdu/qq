/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quizzqq.proyecto.Pregunta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER
 */

@Repository
public class RespuestaRepository {

    private final JdbcTemplate jdbcTemplate;

    public RespuestaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Respuesta> findByPreguntaId(Integer preguntaId) {
        String sql = "SELECT * FROM respuestas WHERE id_pregunta = ?";
        return jdbcTemplate.query(sql, new RespuestaRowMapper(), preguntaId);
    }

    private static final class RespuestaRowMapper implements RowMapper<Respuesta> {
        @Override
        public Respuesta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Respuesta(
                rs.getInt("id"),
                rs.getInt("id_pregunta"),
                rs.getString("texto_respuesta"),
                rs.getString("tipo_respuesta")
            );
        }
    }
}
