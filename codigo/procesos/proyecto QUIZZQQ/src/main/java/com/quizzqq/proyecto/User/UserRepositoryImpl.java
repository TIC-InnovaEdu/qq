package com.quizzqq.proyecto.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class UserRepositoryImpl {

    private final DataSource dataSource;

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT u.id, u.usuario, u.clave, u.estado, p.nombres, p.apellidos " +
                   "FROM usuarios u " +
                   "JOIN personas p ON u.id = p.usuario_id " +
                   "WHERE u.usuario = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsuario(rs.getString("usuario"));
                user.setClave(rs.getString("clave"));
                user.setNombres(rs.getString("nombres"));
                user.setApellidos(rs.getString("apellidos"));
                user.setEstado(rs.getString("estado"));
                user.setRole(Role.USER);
                return Optional.of(user);
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new UsernameNotFoundException("Error al buscar usuario", e);
        }
    }

    public void save(User user) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            // Insertar en usuarios
            String sqlUsuario = "INSERT INTO usuarios (usuario, clave, estado) VALUES (?, ?, ?)";
            PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            
            stmtUsuario.setString(1, user.getUsuario());
            stmtUsuario.setString(2, user.getClave());
            stmtUsuario.setString(3, user.getEstado());
            stmtUsuario.executeUpdate();
            
            // Obtener ID generado
            ResultSet generatedKeys = stmtUsuario.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("No se pudo obtener el ID de usuario");
            }
            int usuarioId = generatedKeys.getInt(1);

            // Insertar en personas
            String sqlPersona = "INSERT INTO personas (nombres, apellidos, usuario_id) VALUES (?, ?, ?)";
            PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona);
            
            stmtPersona.setString(1, user.getNombres());
            stmtPersona.setString(2, user.getApellidos());
            stmtPersona.setInt(3, usuarioId);
            stmtPersona.executeUpdate();
            
            conn.commit();
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}