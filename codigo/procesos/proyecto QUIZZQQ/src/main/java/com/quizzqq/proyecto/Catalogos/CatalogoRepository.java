package com.quizzqq.proyecto.Catalogos;

import com.quizzqq.proyecto.conexion.SQLExecutor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DARWIN ONOFRE
 */
@Repository
public class CatalogoRepository {

    public List<Catalogo> findAllActive() {
        String sql = "SELECT * FROM catalogos WHERE estado = 'A'";

        try {
            return SQLExecutor.executeQuery(sql, rs -> {
                List<Catalogo> catalogos = new ArrayList<>();
                while (rs.next()) {
                    catalogos.add(new Catalogo(
                            rs.getInt("id"),
                            rs.getString("id_catalogo"),
                            rs.getString("id_codigo_catalogo"),
                            rs.getString("descripcion"),
                            rs.getString("estado")
                    ));
                }
                return catalogos;
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener catálogos", e);
        }
    }
}
