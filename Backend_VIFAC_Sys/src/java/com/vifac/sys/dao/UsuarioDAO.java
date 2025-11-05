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

    // -------------------------------------------------------------------------
    // 1. CONSULTA SQL MEJORADA (CON JOIN Y CAMPOS DE PERFIL)
    // Se ha añadido 'u.fotoPerfil'
    // -------------------------------------------------------------------------

    private static final String SQL_PERFIL_COMPLETO = 
    "SELECT "
    + "u.idUsuario, u.nombre, u.documento, u.telefono, u.email, u.direccion, u.nombreUsuario, "
    + "u.contrasena, u.cargo, u.idRol, u.idEmpresa, u.estado, u.intentosFallidos, u.token_recuperacion, u.token_expira, u.fotoPerfil, " 
    + "e.razon_social AS empresa, " // Mapeado como 'empresa' en el ResultSet
    + "CASE u.idRol " // Lógica CASE para Dependencia (CORREGIDA)
    + "WHEN 1 THEN 'Administración' " // ID 1: Administrador
    + "WHEN 2 THEN 'Ventas' " // ID 2: Vendedor (Corregido de 'Logística/Bodega')
    + "WHEN 3 THEN 'Finanzas/Contabilidad' " // ID 3: Contador (Añadido)
    + "WHEN 4 THEN 'Logística/Bodega' " // ID 4: Bodeguero (Logístico) (Añadido y Correcto)
    + "ELSE 'Operaciones' " // Para cualquier otro rol no mapeado
    + "END AS dependencia, " // Mapeado como 'dependencia' en el ResultSet
    + "u.estado AS situacionLaboral, " // Mapeado como 'situacionLaboral'
    + "CONCAT('Perfil extraído el ', DATE_FORMAT(NOW(), '%Y-%m-%d')) AS notaSistema " // Campo extra fijo
    + "FROM usuario u "
    + "LEFT JOIN empresa e ON u.idEmpresa = e.idEmpresa "
    + "WHERE u.nombreUsuario = ?";
    
    // -------------------------------------------------------------------------

    /**
     * Valida las credenciales de un usuario.
     * **CORREGIDO** para usar la consulta completa y el constructor de 19 argumentos.
     * @param nombreUsuario
     * @param contrasena
     * @return
     */
    public Usuario validarUsuario(String nombreUsuario, String contrasena) {
        Usuario usuario = null;
        // Se usa una consulta simple para obtener el hash y el estado, 
        // y luego se llama a una función que recupera el perfil completo.
        String sqlAuth = "SELECT contrasena, intentosFallidos, estado FROM usuario WHERE nombreUsuario = ?";
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmtAuth = conexion.prepareStatement(sqlAuth)) {
            
            stmtAuth.setString(1, nombreUsuario);
            ResultSet rsAuth = stmtAuth.executeQuery();

            if (rsAuth.next()) {
                String contrasenaHash = rsAuth.getString("contrasena");
                int intentosFallidos = rsAuth.getInt("intentosFallidos");
                String estado = rsAuth.getString("estado");

                // Verificar si el usuario está bloqueado
                if ("BLOQUEADO".equalsIgnoreCase(estado)) {
                    LOGGER.log(Level.WARNING, "Usuario bloqueado: {0}", nombreUsuario);
                    return null;
                }

                // Validar contraseña
                if (BCrypt.checkpw(contrasena, contrasenaHash)) {
                    // ✅ Usuario válido, reiniciar intentos fallidos
                    reiniciarIntentosFallidos(nombreUsuario);

                    // --- PASO CLAVE: Recuperar PERFIL COMPLETO (19 campos) ---
                    usuario = obtenerPerfilCompletoPorNombreUsuario(nombreUsuario);
                    // ----------------------------------------------------------
                    
                } else {
                    // ❌ Contraseña incorrecta: aumentar contador
                    incrementarIntentosFallidos(nombreUsuario);

                    // Si supera los 3 intentos, bloquear
                    if (intentosFallidos + 1 >= 3) {
                        bloquearUsuario(nombreUsuario);
                        LOGGER.log(Level.WARNING, "Usuario bloqueado por m\u00faltiples intentos fallidos: {0}", nombreUsuario);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al validar el usuario: " + nombreUsuario, e);
        }
        return usuario;
    }

    /**
     * **NUEVO MÉTODO AUXILIAR**
     * Obtiene todos los campos del usuario (19 campos incluyendo JOINs) después de validar la contraseña.
     * @param nombreUsuario
     * @return
     */
    private Usuario obtenerPerfilCompletoPorNombreUsuario(String nombreUsuario) {
        Usuario usuario = null;
        // Reutilizamos la constante, el .replace() es redundante pero se mantiene el patrón.
        String sql = SQL_PERFIL_COMPLETO.replace("WHERE u.nombreUsuario = ?", "WHERE u.nombreUsuario = ?"); 
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    rs.getString("documento"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion"),
                    rs.getString("nombreUsuario"),
                    null, // No exponer contrasena
                    rs.getString("cargo"),
                    rs.getInt("idRol"),
                    rs.getInt("idEmpresa"), // <-- NUEVO ARGUMENTO (11)
                    rs.getString("estado"),
                    rs.getInt("intentosFallidos"),
                    rs.getString("token_recuperacion"),
                    rs.getTimestamp("token_expira"),
                    
                    // --- 4 ARGUMENTOS EXTRA DEL JOIN (16-19) ---
                    rs.getString("empresa"), // Alias e.razon_social AS empresa
                    rs.getString("dependencia"), // Lógica CASE AS dependencia
                    rs.getString("situacionLaboral"), // Alias u.estado AS situacionLaboral
                    rs.getString("notaSistema"), // Alias 'texto fijo' AS notaSistema
                    rs.getString("fotoPerfil") // <-- AÑADIDO
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener perfil completo para: " + nombreUsuario, e);
        }
        return usuario;
    }


    /**
     * Incrementa el contador de intentos fallidos del usuario.
     */
    private void incrementarIntentosFallidos(String nombreUsuario) {
        String sql = "UPDATE usuario SET intentosFallidos = intentosFallidos + 1 WHERE nombreUsuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al incrementar intentos fallidos para el usuario: " + nombreUsuario, e);
        }
    }

    /**
     * Reinicia el contador de intentos fallidos del usuario a cero.
     */
    private void reiniciarIntentosFallidos(String nombreUsuario) {
        String sql = "UPDATE usuario SET intentosFallidos = 0 WHERE nombreUsuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al reiniciar intentos fallidos para el usuario: " + nombreUsuario, e);
        }
    }

    /**
     * Bloquea al usuario cambiando su estado a 'BLOQUEADO'.
     */
    private void bloquearUsuario(String nombreUsuario) {
        String sql = "UPDATE usuario SET estado = 'BLOQUEADO' WHERE nombreUsuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al bloquear el usuario: " + nombreUsuario, e);
        }
    }


    /**
     * Agrega un nuevo usuario a la base de datos.
     * **CORREGIDO** para incluir `idEmpresa` y `fotoPerfil`.
     * @param u
     * @return
     */
public boolean agregarUsuario(Usuario u) {
    String sql = "INSERT INTO usuario(nombre, documento, telefono, email, direccion, nombreUsuario, contrasena, cargo, idRol, idEmpresa, estado, intentosFallidos, token_recuperacion, token_expira, fotoPerfil) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {

        stmt.setString(1, u.getNombre());
        stmt.setString(2, u.getDocumento());
        stmt.setString(3, u.getTelefono());
        stmt.setString(4, u.getEmail());
        stmt.setString(5, u.getDireccion());
        stmt.setString(6, u.getNombreUsuario());
        stmt.setString(7, BCrypt.hashpw(u.getContrasena(), BCrypt.gensalt()));
        stmt.setString(8, u.getCargo());
        stmt.setInt(9, u.getIdRol());
        stmt.setInt(10, u.getIdEmpresa());
        stmt.setString(11, u.getEstado());
        stmt.setInt(12, u.getIntentosFallidos());
        stmt.setString(13, null);
        stmt.setTimestamp(14, null);
        stmt.setString(15, null);

        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al agregar usuario.", e);
        return false;
    }
}

    
    /**
     * Actualiza la información de un usuario existente, incluyendo la contraseña si se proporciona.
     * **CORREGIDO** para incluir `idEmpresa` y `fotoPerfil`.
     * @param u Usuario con los nuevos datos.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarUsuario(Usuario u) {
        StringBuilder sql = new StringBuilder("UPDATE usuario SET nombre=?, documento=?, telefono=?, email=?, direccion=?, nombreUsuario=?, cargo=?, idRol=?, idEmpresa=?, estado=?, token_recuperacion=?, token_expira=?, fotoPerfil=?");
        
        // Si se envía una nueva contraseña, se incluye en la actualización
        boolean actualizarContrasena = u.getContrasena() != null && !u.getContrasena().trim().isEmpty();
        if (actualizarContrasena) {
            sql.append(", contrasena=?, intentosFallidos=0");
        }
        
        sql.append(" WHERE idUsuario=?");
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql.toString())) {
            
            int index = 1;
            stmt.setString(index++, u.getNombre());
            stmt.setString(index++, u.getDocumento());
            stmt.setString(index++, u.getTelefono());
            stmt.setString(index++, u.getEmail());
            stmt.setString(index++, u.getDireccion());
            stmt.setString(index++, u.getNombreUsuario());
            stmt.setString(index++, u.getCargo());
            stmt.setInt(index++, u.getIdRol());
            stmt.setInt(index++, u.getIdEmpresa());
            stmt.setString(index++, u.getEstado());
            stmt.setString(index++, u.getTokenRecuperacion());
            stmt.setTimestamp(index++, u.getTokenExpira());
            stmt.setString(index++, u.getFotoPerfil());
            
            // Si hay contraseña nueva, la encripta y agrega
            if (actualizarContrasena) {
                String hash = BCrypt.hashpw(u.getContrasena(), BCrypt.gensalt());
                stmt.setString(index++, hash);
            }
            
            stmt.setInt(index, u.getIdUsuario());
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al actualizar usuario con ID: " + u.getIdUsuario(), e);
            return false;
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * **CORREGIDO** para usar la consulta completa (SQL_PERFIL_COMPLETO) y el constructor de 19 argumentos.
     * @param idUsuario identificador del usuario.
     * @return objeto Usuario con los datos, o null si no se encuentra.
     */
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        Usuario usuario = null;
        // Usamos una consulta similar a SQL_PERFIL_COMPLETO, pero buscando por ID
        String sql = SQL_PERFIL_COMPLETO.replace("WHERE u.nombreUsuario = ?", "WHERE u.idUsuario = ?");
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    rs.getString("documento"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion"),
                    rs.getString("nombreUsuario"),
                    null, // Contraseña
                    rs.getString("cargo"),
                    rs.getInt("idRol"),
                    rs.getInt("idEmpresa"), // <-- ARGUMENTO 11
                    rs.getString("estado"),
                    rs.getInt("intentosFallidos"),
                    rs.getString("token_recuperacion"),
                    rs.getTimestamp("token_expira"),
                    
                    // --- 4 ARGUMENTOS EXTRA DEL JOIN (16-19) ---
                    rs.getString("empresa"), 
                    rs.getString("dependencia"), 
                    rs.getString("situacionLaboral"), 
                    rs.getString("notaSistema"),
                    rs.getString("fotoPerfil") // <-- AÑADIDO
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuario por ID: " + idUsuario, e);
        }
        return usuario;
    }

    /**
     * Elimina un usuario por su ID.
     * @param idUsuario
     * @return
     */
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

    /**
     * Busca usuarios por nombre, nombre de usuario o documento.
     * **CORREGIDO** para usar el constructor de 19 argumentos.
     * **NOTA IMPORTANTE:** Esta consulta *NO* hace JOIN, por lo que los 4 campos extra (empresa, dependencia, situacionLaboral, notaSistema) serán `null`.
     * @param criterio
     * @return
     */
    public List<Usuario> buscarUsuarios(String criterio) {
        List<Usuario> lista = new ArrayList<>();
        // Asumiendo que fotoPerfil es una columna directa en la tabla 'usuario'
        String sql = "SELECT idUsuario, nombre, documento, telefono, email, direccion, nombreUsuario, cargo, idRol, idEmpresa, estado, intentosFallidos, token_recuperacion, token_expira, fotoPerfil FROM usuario WHERE nombre LIKE ? OR nombreUsuario LIKE ? OR documento LIKE ?";
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
                    rs.getString("direccion"),
                    rs.getString("nombreUsuario"),
                    null, // Contraseña no se carga (8)
                    rs.getString("cargo"),
                    rs.getInt("idRol"),
                    rs.getInt("idEmpresa"), // <-- ARGUMENTO 11
                    rs.getString("estado"),
                    rs.getInt("intentosFallidos"),
                    rs.getString("token_recuperacion"),
                    rs.getTimestamp("token_expira"),
                    // --- 4 ARGUMENTOS EXTRA (16-19) DUMMY ---
                    null, // empresa
                    null, // dependencia
                    null, // situacionLaboral
                    null,  // notaSistema
                    rs.getString("fotoPerfil") // <-- AÑADIDO
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al buscar usuarios con el criterio: " + criterio, e);
        }
        return lista;
    }

    /**
     * Lista todos los usuarios.
     * **CORREGIDO** para usar el constructor de 19 argumentos.
     * **NOTA IMPORTANTE:** Esta consulta *NO* hace JOIN, por lo que los 4 campos extra (empresa, dependencia, situacionLaboral, notaSistema) serán `null`.
     * @return
     */
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        // Se ha añadido 'fotoPerfil' a la consulta SQL
        String sql = "SELECT idUsuario, nombre, documento, telefono, email, direccion, nombreUsuario, cargo, idRol, idEmpresa, estado, intentosFallidos, token_recuperacion, token_expira, fotoPerfil FROM usuario"; 
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombre"),
                    rs.getString("documento"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion"),
                    rs.getString("nombreUsuario"),
                    null, // Contraseña no se carga (8)
                    rs.getString("cargo"),
                    rs.getInt("idRol"),
                    rs.getInt("idEmpresa"), // <-- ARGUMENTO 11
                    rs.getString("estado"),
                    rs.getInt("intentosFallidos"),
                    rs.getString("token_recuperacion"),
                    rs.getTimestamp("token_expira"),
                    // --- 4 ARGUMENTOS EXTRA (16-19) DUMMY ---
                    null, // empresa
                    null, // dependencia
                    null, // situacionLaboral
                    null,  // notaSistema
                    rs.getString("fotoPerfil") // <-- AÑADIDO
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar usuarios.", e);
        }
        return lista;
    }

    // ================= RECUPERACIÓN DE CONTRASEÑA =================
    
    /**
     * Busca un usuario por su dirección de email.
     * **CORREGIDO** para usar el constructor de 19 argumentos.
     * **NOTA IMPORTANTE:** Esta consulta *NO* hace JOIN, por lo que los 4 campos extra serán `null`.
     * @param email
     * @return
     */
    public Usuario buscarPorEmail(String email) {
         // Se ha añadido 'fotoPerfil' a la consulta SQL
        String sql = "SELECT idUsuario, nombre, documento, telefono, email, direccion, nombreUsuario, cargo, idRol, idEmpresa, estado, intentosFallidos, token_recuperacion, token_expira, fotoPerfil FROM usuario WHERE email = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("nombreUsuario"),
                        null, // Contraseña no se carga (8)
                        rs.getString("cargo"),
                        rs.getInt("idRol"),
                        rs.getInt("idEmpresa"), // <-- ARGUMENTO 11
                        rs.getString("estado"),
                        rs.getInt("intentosFallidos"),
                        rs.getString("token_recuperacion"),
                        rs.getTimestamp("token_expira"),
                        // --- 4 ARGUMENTOS EXTRA (16-19) DUMMY ---
                        null, // empresa
                        null, // dependencia
                        null, // situacionLaboral
                        null,  // notaSistema
                        rs.getString("fotoPerfil") // <-- AÑADIDO
                    );
                    return u;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario por email: " + email, e);
        }
        return null;
    }

    /**
     * Guarda el token de recuperación y su expiración para un email dado.
     * @param email
     * @param token
     * @return
     */
    public boolean guardarTokenRecuperacion(String email, String token) {
        String sql = "UPDATE usuario SET token_recuperacion = ?, token_expira = DATE_ADD(NOW(), INTERVAL 1 HOUR) WHERE email = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar token recuperación para email: " + email, e);
            return false;
        }
    }
    
    /**
     * Guarda el token de recuperación y su expiración para un ID de usuario.
     * @param idUsuario
     * @param token
     * @return
     */
    public boolean guardarTokenRecuperacion(int idUsuario, String token) {
        String sql = "UPDATE usuario SET token_recuperacion = ?, token_expira = DATE_ADD(NOW(), INTERVAL 1 HOUR) WHERE idUsuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar token recuperación para idUsuario: " + idUsuario, e);
            return false;
        }
    }

    /**
     * Verifica si existe un usuario con el email dado.
     * @param email
     * @return
     */
    public boolean existeUsuarioPorEmail(String email) {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
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

    /**
     * Actualiza la contraseña si el token es válido y no ha expirado.
     * @param token
     * @param nuevaContrasena
     * @return
     */
    public boolean actualizarContrasenaPorToken(String token, String nuevaContrasena) {
        String sql = "UPDATE usuario SET contrasena = ?, token_recuperacion = NULL, token_expira = NULL, intentosFallidos = 0 WHERE token_recuperacion = ? AND token_expira > NOW()";
        try (Connection conexion = ConexionBD.obtenerConexion();
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

    /**
     * Actualiza solo el teléfono y la dirección.
     * @param u
     * @return 
     */
    public boolean actualizarDatosPerfil(Usuario u) {
    String sql = "UPDATE usuario SET telefono = ?, direccion = ? WHERE idUsuario = ?";
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {
        stmt.setString(1, u.getTelefono());
        stmt.setString(2, u.getDireccion());
        stmt.setInt(3, u.getIdUsuario());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al actualizar datos de perfil del usuario ID: " + u.getIdUsuario(), e);
        return false;
    }
}
/**
     * Actualiza solo la ruta de la foto de perfil para un usuario.
     * @param idUsuario ID del usuario.
     * @param fotoPerfil Ruta o nombre del archivo de la foto.
     * @return true si la actualización fue exitosa.
     */
    public boolean actualizarFotoPerfil(int idUsuario, String fotoPerfil) {
        String sql = "UPDATE usuario SET fotoPerfil = ? WHERE idUsuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, fotoPerfil);
            stmt.setInt(2, idUsuario);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar la foto de perfil del usuario ID: " + idUsuario, e);
            return false;
        }
    }
}