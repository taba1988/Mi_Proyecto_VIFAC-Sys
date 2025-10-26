/**
 * PasswordUtil
 * 
 * Clase de utilidad para el manejo seguro de contraseñas.
 * 
 * Funcionalidades:
 * 1. hashear(String contrasena): Genera un hash seguro usando BCrypt,
 *    compatible con UsuarioDAO.
 * 2. esSegura(String contrasena): Valida que la contraseña cumpla con requisitos mínimos
 *    de seguridad, incluyendo longitud mínima de 8 caracteres, al menos una letra mayúscula,
 *    una letra minúscula, un número y un carácter especial.
 * 
 * Esta clase permite que cada usuario gestione su propia contraseña de forma segura
 * sin depender de credenciales externas o del administrador del sistema.
 * 
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 24/10/2025
 */

package com.vifac.sys.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Método para hashear la contraseña usando BCrypt
    public static String hashear(String contrasena) {
        // Genera un hash con salt automático
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(12));
    }

    // Método para verificar que la contraseña en texto plano coincide con el hash
    public static boolean verificar(String contrasena, String hash) {
        return BCrypt.checkpw(contrasena, hash);
    }

    // Método para validar seguridad mínima de la contraseña
    public static boolean esSegura(String contrasena) {
        return contrasena != null &&
               contrasena.length() >= 8 &&
               contrasena.matches(".*[A-Z].*") &&
               contrasena.matches(".*[a-z].*") &&
               contrasena.matches(".*\\d.*") &&
               contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
}
