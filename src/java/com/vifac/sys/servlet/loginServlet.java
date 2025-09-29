
/*
 * loginServlet para manejar el proceso de inicio de sesión.
 * Verifica credenciales usando UsuarioDAO, gestiona intentos fallidos
 * y guarda datos relevantes del usuario en sesión si la autenticación es exitosa.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Crear instancia del DAO como atributo de la clase para reutilización
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasenaIngresada = request.getParameter("contrasena");

        System.out.println("--- Depuración de Login ---");
        System.out.println("Usuario recibido: '" + nombreUsuario + "'");
        System.out.println("Contraseña recibida: '" + contrasenaIngresada + "'");

        HttpSession session = request.getSession();
        Integer intentosFallidos = (Integer) session.getAttribute("intentosFallidos");

        if (intentosFallidos == null) {
            intentosFallidos = 0;
        }

        if (intentosFallidos >= 3) {
            System.out.println("Demasiados intentos fallidos para el usuario: " + nombreUsuario);
            request.setAttribute("loginError", "Demasiados intentos fallidos. Intente más tarde.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Validar usuario con DAO (no estático)
        Usuario usuario = usuarioDAO.validarUsuario(nombreUsuario, contrasenaIngresada);

        if (usuario != null && "Inactivo".equalsIgnoreCase(usuario.getEstado())) {
            System.out.println("Usuario inactivo: " + nombreUsuario);
            request.setAttribute("loginError", "El usuario está inactivo.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (usuario != null) {
            // Inicio de sesión exitoso
            System.out.println("Inicio de sesión exitoso para: " + usuario.getNombre());

            session.setAttribute("usuarioAutenticado", usuario.getNombreUsuario());
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("nombre", usuario.getNombre());
            session.setAttribute("documento", usuario.getDocumento());
            session.setAttribute("telefono", usuario.getTelefono());
            session.setAttribute("email", usuario.getEmail());
            session.setAttribute("cargo", usuario.getCargo());
            session.setAttribute("idRol", usuario.getIdRol());
            session.setAttribute("estado", usuario.getEstado());
            session.setAttribute("intentosFallidosBD", usuario.getIntentosFallidos());
            session.setAttribute("usuarioLogeado", usuario);

            // Resetear intentos fallidos en sesión
            session.removeAttribute("intentosFallidos");

            // Cookies de "recordar usuario"
            if ("on".equalsIgnoreCase(request.getParameter("remember"))) {
                Cookie userCookie = new Cookie("rememberMe", nombreUsuario);
                userCookie.setMaxAge(60 * 60 * 24 * 7); // 7 días
                response.addCookie(userCookie);
            } else {
                Cookie userCookie = new Cookie("rememberMe", "");
                userCookie.setMaxAge(0);
                response.addCookie(userCookie);
            }

            // Redirigir a página principal
            response.sendRedirect(request.getContextPath() + "/index.jsp?mensajeBienvenida=true");

        } else {
            // Credenciales incorrectas
            System.out.println("Credenciales incorrectas para el usuario: " + nombreUsuario);
            intentosFallidos++;
            session.setAttribute("intentosFallidos", intentosFallidos);
            request.setAttribute("loginError", "Usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}