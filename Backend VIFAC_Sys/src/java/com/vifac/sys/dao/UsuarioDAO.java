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

    // Validar usuario (login)
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
                        // No devolver la contraseña, ya validada
                        null, 
                        rs.getString("cargo"),
                        rs.getInt("idRol"),
                        rs.getString("estado"),
                        rs.getInt("intentosFallidos")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al validar el usuario: " + nombreUsuario, e);
        }
        return usuario;
    }

    // Agregar usuario
    public boolean agregarUsuario(Usuario u) {
        String sql = "INSERT INTO usuario(nombre, documento, telefono, email, nombreUsuario, contrasena, cargo, idRol, estado, intentosFallidos) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getDocumento());
            stmt.setString(3, u.getTelefono());
            stmt.setString(4, u.getEmail());
            stmt.setString(5, u.getNombreUsuario());

            String hashed = BCrypt.hashpw(u.getContrasena(), BCrypt.gensalt());
            stmt.setString(6, hashed);

            stmt.setString(7, u.getCargo());
            stmt.setInt(8, u.getIdRol());
            stmt.setString(9, u.getEstado());
            stmt.setInt(10, u.getIntentosFallidos());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al agregar un usuario.", e);
        }
        return false;
    }

    // Actualizar usuario
    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuario SET nombre=?, documento=?, telefono=?, email=?, nombreUsuario=?, cargo=?, idRol=?, estado=? WHERE idUsuario=?";
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
            stmt.setInt(9, u.getIdUsuario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al actualizar el usuario con ID: " + u.getIdUsuario(), e);
        }
        return false;
    }

    // Eliminar usuario
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE idUsuario=?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al eliminar el usuario con ID: " + idUsuario, e);
        }
        return false;
    }

    // Buscar usuarios (por nombre, usuario o documento)
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
                    // Omitir la contraseña
                    null,
                    rs.getString("cargo"),
                    rs.getInt("idRol"),
                    rs.getString("estado"),
                    rs.getInt("intentosFallidos")
                );
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

            lista.add(u);
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error de SQL al listar usuarios.", e);
    }

    // Debug: mostrar cuantos usuarios se recuperaron
    System.out.println("Cantidad de usuarios recuperados: " + lista.size());

    return lista;
}


    public void editarUsuario(Usuario usuarioEditado) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
