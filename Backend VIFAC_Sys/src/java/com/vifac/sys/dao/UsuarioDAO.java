/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de usuarios.
 * Su función principal es validar las credenciales de un usuario,
 * comparando la contraseña ingresada con el hash almacenado en la base de datos.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 6/09/2024
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Usuario;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(UsuarioDAO.class.getName());

    public Usuario validarUsuario(String nombreUsuario, String contrasena) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE nombreUsuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String contrasenaHash = rs.getString("contrasena");
                if (BCrypt.checkpw(contrasena, contrasenaHash)) {
                    usuario = new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("nombreUsuario"),
                        null, 
                        rs.getString("cargo"),
                        rs.getInt("idRol"),
                        rs.getString("estado"),
                        rs.getInt("intentosFallidos")
                    );
                    usuario.setTokenRecuperacion(rs.getString("token_recuperacion"));
                    usuario.setTokenExpira(rs.getTimestamp("token_expira"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al validar el usuario: " + nombreUsuario, e);
        }
        return usuario;
    }

     // Agregar usuario
    public boolean agregarUsuario(Usuario u) {
        String sql = "INSERT INTO usuario(nombre, documento, telefono, email, nombreUsuario, contrasena, cargo, idRol, estado, intentosFallidos, token_recuperacion, token_expira) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getDocumento());
            stmt.setString(3, u.getTelefono());
            stmt.setString(4, u.getEmail());
            stmt.setString(5, u.getNombreUsuario());
            stmt.setString(6, BCrypt.hashpw(u.getContrasena(), BCrypt.gensalt()));
            stmt.setString(7, u.getCargo());
            stmt.setInt(8, u.getIdRol());
            stmt.setString(9, u.getEstado());
            stmt.setInt(10, u.getIntentosFallidos());
            stmt.setString(11, u.getTokenRecuperacion());
            stmt.setTimestamp(12, u.getTokenExpira());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar usuario.", e);
            return false;
        }
    }
    // Actualizar usuario
    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuario SET nombre=?, documento=?, telefono=?, email=?, nombreUsuario=?, cargo=?, idRol=?, estado=?, token_recuperacion=?, token_expira=? WHERE idUsuario=?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getDocumento());
            stmt.setString(3, u.getTelefono());
            stmt.setString(4, u.getEmail());
            stmt.setString(5, u.getNombreUsuario());
            stmt.setString(6, u.getCargo());
            stmt.setInt(7, u.getIdRol());
            stmt.setString(8, u.getEstado());
            stmt.setString(9, u.getTokenRecuperacion());
            stmt.setTimestamp(10, u.getTokenExpira());
            stmt.setInt(11, u.getIdUsuario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al actualizar el usuario con ID: " + u.getIdUsuario(), e);
            return false;
        }
    }
       // Eliminar usuario
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE idUsuario=?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar usuario ID: " + idUsuario, e);
            return false;
        }
    }

    public List<Usuario> buscarUsuarios(String criterio) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE nombre LIKE ? OR nombreUsuario LIKE ? OR documento LIKE ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            String busq = "%" + criterio + "%";
            stmt.setString(1, busq);
            stmt.setString(2, busq);
            stmt.setString(3, busq);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    rs.getString("documento"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("nombreUsuario"),
                    null,
                    rs.getString("cargo"),
                    rs.getInt("idRol"),
                    rs.getString("estado"),
                    rs.getInt("intentosFallidos")
                );
                u.setTokenRecuperacion(rs.getString("token_recuperacion"));
                u.setTokenExpira(rs.getTimestamp("token_expira"));
                lista.add(u);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al buscar usuarios con el criterio: " + criterio, e);
        }
        return lista;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setNombre(rs.getString("nombre"));
                u.setDocumento(rs.getString("documento"));
                u.setTelefono(rs.getString("telefono"));
                u.setEmail(rs.getString("email"));
                u.setNombreUsuario(rs.getString("nombreUsuario"));
                u.setCargo(rs.getString("cargo"));
                u.setIdRol(rs.getInt("idRol"));
                u.setEstado(rs.getString("estado"));
                u.setIntentosFallidos(rs.getInt("intentosFallidos"));
                u.setTokenRecuperacion(rs.getString("token_recuperacion"));
                u.setTokenExpira(rs.getTimestamp("token_expira"));
                lista.add(u);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar usuarios.", e);
        }
        return lista;
    }

    // ================= RECUPERACIÓN DE CONTRASEÑA =================

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("idUsuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setDocumento(rs.getString("documento"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setEmail(rs.getString("email"));
                    u.setNombreUsuario(rs.getString("nombreUsuario"));
                    u.setCargo(rs.getString("cargo"));
                    u.setIdRol(rs.getInt("idRol"));
                    u.setEstado(rs.getString("estado"));
                    u.setIntentosFallidos(rs.getInt("intentosFallidos"));
                    u.setTokenRecuperacion(rs.getString("token_recuperacion"));
                    u.setTokenExpira(rs.getTimestamp("token_expira"));
                    return u;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario por email: " + email, e);
        }
        return null;
    }

    public boolean guardarTokenRecuperacion(String email, String token) {
        String sql = "UPDATE usuario SET token_recuperacion = ?, token_expira = DATE_ADD(NOW(), INTERVAL 1 HOUR) WHERE email = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar token recuperación para email: " + email, e);
            return false;
        }
    }

    // ==== NUEVO MÉTODO: guardar token por idUsuario ====
    public boolean guardarTokenRecuperacion(int idUsuario, String token) {
        String sql = "UPDATE usuario SET token_recuperacion = ?, token_expira = DATE_ADD(NOW(), INTERVAL 1 HOUR) WHERE idUsuario = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar token recuperación para idUsuario: " + idUsuario, e);
            return false;
        }
    }

    public boolean existeUsuarioPorEmail(String email) {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar existencia de usuario por email: " + email, e);
            return false;
        }
    }

public boolean actualizarContrasenaPorToken(String token, String nuevaContrasena) {
    String sql = "UPDATE usuario SET contrasena = ?, token_recuperacion = NULL, token_expira = NULL WHERE token_recuperacion = ? AND token_expira > NOW()";
    try (Connection conexion = obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {
        String hash = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());
        stmt.setString(1, hash);
        stmt.setString(2, token);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al actualizar contraseña por token: " + token, e);
        return false;
    }
}

}
