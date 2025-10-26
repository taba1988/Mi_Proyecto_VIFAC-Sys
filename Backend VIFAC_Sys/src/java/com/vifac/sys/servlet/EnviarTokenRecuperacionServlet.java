package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.RespuestaJson;
import com.vifac.sys.modelo.Usuario;
import com.vifac.sys.util.MailSender;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EnviarTokenRecuperacionServlet")
public class EnviarTokenRecuperacionServlet extends HttpServlet {

    // Se recomienda usar inyección de dependencias si usas Spring, pero para Servlets puros, esto es aceptable.
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();
    private static final Logger LOGGER = Logger.getLogger(EnviarTokenRecuperacionServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<script>alert('Se envió un enlace de recuperación a tu correo. Revisa tu bandeja.'); window.location='login.jsp';</script>");


        String email = request.getParameter("email");
        RespuestaJson respuesta;

        try {
            if (email == null || email.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                respuesta = new RespuestaJson("error", "Debe ingresar un correo válido.");
                response.getWriter().write(gson.toJson(respuesta));
                return;
            }

            Usuario usuario = usuarioDAO.buscarPorEmail(email);

            // Importante: Lógica de seguridad para evitar enumeración de usuarios
            if (usuario == null) {
                // Damos un mensaje genérico, pero aún respondemos con éxito (status 200)
                respuesta = new RespuestaJson("success", "Si el correo está registrado, se enviará el enlace de recuperación.");
                response.getWriter().write(gson.toJson(respuesta));
                return;
            }

            // --- Proceso de Generación y Guardado de Token ---
            String token = generarTokenSeguro();
            // Esta llamada debe ser segura y no fallar.
            usuarioDAO.guardarTokenRecuperacion(usuario.getIdUsuario(), token); 

            // Construir el link (ajustar a tu URL real de restablecimiento)
            // Asegúrate que "/RestablecerContrasena.jsp" es la URL correcta para tu siguiente paso
            String linkBase = request.getRequestURL().toString().replace(request.getServletPath(), "");
            String link = linkBase + "/RestablecerContrasena.jsp?token=" + token;

            // --- Envío de Correo ---
            
            System.out.println("Remitente SMTP_USER: " + System.getenv("SMTP_USER"));
            System.out.println("Destinatario: " + usuario.getEmail());
            
            boolean enviado = MailSender.enviarCorreo(
                usuario.getEmail(),
                "Recuperación de contraseña VIFAC-Sys",
                "Hola " + usuario.getNombre() + ",\n\n"
                    + "Haga clic en el siguiente enlace para restablecer su contraseña:\n"
                    + link + "\n\n"
                    + "Este enlace expirará pronto por seguridad. Si no solicitó este cambio, ignore este correo.");

            if (enviado) {
                respuesta = new RespuestaJson("success", "Se ha enviado el enlace de recuperación a su correo.");
            } else {
                // Falla en el envío del correo (ej. credenciales SMTP incorrectas)
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Código 500 si falla el proceso crítico
                respuesta = new RespuestaJson("error", "Error al enviar el correo. Por favor, contacte a soporte.");
            }

        } catch (IOException e) {
            // Manejar cualquier excepción inesperada (ej. error de conexión a BD o Gson missing)
            LOGGER.log(Level.SEVERE, "Error fatal en el Servlet de Recuperación: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Código 500
            respuesta = new RespuestaJson("error", "Ocurrió un error inesperado en el servidor.");
        }
        
        // Asegurarse de que la respuesta se escribe, incluso si es un error 500
        response.getWriter().write(gson.toJson(respuesta));
    }

    /**
     * Genera un token aleatorio seguro (URL-safe Base64).
     * @return Token de 32 caracteres.
     */
    private String generarTokenSeguro() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24]; // 24 bytes = 192 bits, ~32 caracteres en Base64 URL-safe
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
