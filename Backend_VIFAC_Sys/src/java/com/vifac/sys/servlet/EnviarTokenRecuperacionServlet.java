package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.Usuario;
import com.vifac.sys.util.MailSender;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
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

    String email = request.getParameter("email");

    // Validación: campo vacío
    if (email == null || email.isEmpty()) {
        request.setAttribute("tipoModal", "error");
        request.setAttribute("mensajeModal", "Debe ingresar un correo válido.");
        request.getRequestDispatcher("EnviarTokenRecuperacion.jsp").forward(request, response);
        return;
    }

    Usuario usuario = usuarioDAO.buscarPorEmail(email);

    // Correo NO existe
    if (usuario == null) {
        request.setAttribute("tipoModal", "error");
        request.setAttribute("mensajeModal", "No se encontró el correo electrónico proporcionado.");
        request.getRequestDispatcher("EnviarTokenRecuperacion.jsp").forward(request, response);
        return;
    }

    // Generar token
    String token = generarTokenSeguro();
    usuarioDAO.guardarTokenRecuperacion(usuario.getIdUsuario(), token);

    // Crear link
    String linkBase = request.getRequestURL().toString().replace(request.getServletPath(), "");
    String link = linkBase + "/RestablecerContrasena.jsp?token=" + token;

    boolean enviado = MailSender.enviarCorreo(
            usuario.getEmail(),
            "Recuperación de contraseña VIFAC-Sys",
            "Hola " + usuario.getNombre() + ",\n\n"
            + "Haga clic en el siguiente enlace para restablecer su contraseña:\n"
            + link + "\n\n"
            + "Este enlace expira en 15 minutos."
    );

    if (!enviado) {
        request.setAttribute("tipoModal", "error");
        request.setAttribute("mensajeModal", "Error al enviar el correo. Contacte a soporte.");
        request.getRequestDispatcher("EnviarTokenRecuperacion.jsp").forward(request, response);
        return;
    }

    // ÉXITO
    request.setAttribute("tipoModal", "success");
    request.setAttribute("mensajeModal", "Se ha enviado un enlace de recuperación a su correo.  Revisa tu bandeja");
    request.getRequestDispatcher("login.jsp").forward(request, response);
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
