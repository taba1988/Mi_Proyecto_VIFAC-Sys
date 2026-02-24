/**
 * MailSender
 * * Clase de utilidad para enviar correos electrónicos mediante SMTP.
 * * Este sistema permite que cada usuario registrado envíe correos desde su propia cuenta,
 * sin depender de credenciales fijas definidas en el código o variables de entorno.
 * El administrador del sistema no necesita modificar el código ni tener acceso a las 
 * credenciales de otros usuarios. Cada usuario puede usar su correo personal o corporativo
 * para recibir notificaciones o enlaces de recuperación de contraseña.
 * * Esta clase valida que el correo del remitente y destinatario estén correctamente configurados
 * para evitar errores comunes al enviar correos.
 * * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 24/10/2025
 */

package com.vifac.sys.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;


public class MailSender {

    /**
     * Envía un correo electrónico a un destinatario con asunto y cuerpo.* @param destinatario Correo electrónico del receptor (no nulo, formato válido)
     * @param destinatario
     * @param asunto      Asunto del correo
     * @param cuerpo      Contenido del correo
     * @return true si el correo se envía correctamente, false en caso de error
     */
    public static boolean enviarCorreo(String destinatario, String asunto, String cuerpo) {
        // Leer credenciales desde variables de entorno
        String remitente = System.getenv("SMTP_USER");
        String clave = System.getenv("SMTP_PASS");
        
        System.out.println("SMTP_USER: " + System.getenv("SMTP_USER"));
        System.out.println("SMTP_PASS: " + System.getenv("SMTP_PASS"));
        
        /*
        * CONFIGURACIÓN EXTERNA PARA ENVIO DE CORREO LOCAL
        * ---------------------------------------------------------
        * Las credenciales SMTP no se almacenan en el código fuente.
        * Se obtienen desde variables de entorno definidas en el
        * archivo setenv.bat del servidor de aplicaciones (Tomcat) apache-tomcat-8.5.100\bin\setenv.bat.
        *
        * Para actualizar usuario o contraseña geenrado en contraseña de Aplicaciones en Google:
        * 1. Modificar setenv.bat
        * 2. Reiniciar el servidor
        */

        if (remitente == null || remitente.isEmpty() || clave == null || clave.isEmpty() || destinatario == null || destinatario.trim().isEmpty()) {
            return false;
        }
        if (clave.isEmpty()) {
            System.err.println("Error: SMTP_PASS no definido");
            return false;
        }
        if (destinatario.trim().isEmpty()) {
            System.err.println("Error: destinatario nulo o vacío");
            return false;
        }

        // Validar que el destinatario tenga un formato de correo válido
        try {
            InternetAddress emailAddr = new InternetAddress(destinatario);
            emailAddr.validate();
        } catch (AddressException ex) {
            System.err.println("Error: correo destinatario inválido -> " + destinatario);
            return false;
        }

         // Configuración para BREVO SMTP (Funciona en Local y Render)
         Properties props = new Properties();
         System.setProperty("java.net.preferIPv4Stack", "true"); 

         props.put("mail.smtp.host", "smtp-relay.brevo.com");
         props.put("mail.smtp.port", "587");
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.starttls.required", "true");
         props.put("mail.smtp.connectiontimeout", "15000");
         props.put("mail.smtp.timeout", "15000");

        // Crear sesión autenticada con las credenciales
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            // Crear el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("soporte.tecnico.vifac@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);

            // Enviar correo
            Transport.send(message);
            System.out.println("Correo enviado a: " + destinatario);
            return true;

        } catch (MessagingException e) {
            // ** MODIFICACIÓN CLAVE PARA DEPURACIÓN **
            System.err.println("------------------------------------------");
            System.err.println("ERROR SMTP CRÍTICO - VER TRAZA ABAJO:");
            e.printStackTrace(System.err); // ¡Esto nos dirá si es fallo de Auth o Conexión!
            System.err.println("------------------------------------------");
            return false;
        }
    }

    // IMPLEMENTACIÓN DEL ENVÍO DE CORREO AL INICIAR SESIÓN CON FECHA, HORA E IP
    public static void enviarCorreoLogin(String email, String nombre, HttpServletRequest request) {
        // Obtener IP real del usuario
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }

        String fechaHora = LocalDateTime.now().toString();
        String asunto = "Inicio de sesión exitoso - Sistema VIFAC";
        String cuerpo = "Hola " + nombre + ",\n\n"
                        + "Se ha detectado un inicio de sesión exitoso en tu cuenta del sistema VIFAC.\n\n"
                        + "Detalles de acceso:\n"
                        + "Fecha y hora: " + fechaHora + "\n"
                        + "IP de acceso: " + ip + "\n\n"
                        + "Si fuiste tú, ¡perfecto! Continúa usando el sistema con normalidad.\n"
                
                        + "Si NO fuiste tú, por favor restablece tu contraseña de inmediato o comunícate con el administrador.\n\n"
                        + "Atentamente,\n"
                        + "Administrador VIFAC";

        boolean enviado = enviarCorreo(email, asunto, cuerpo);

        if (!enviado) { 
            System.err.println("No se pudo enviar correo de inicio de sesión a: " + email); 
        }
     }
    
    public static void enviarCorreoLogin(String email, String nombre) {
        throw new UnsupportedOperationException("Not supported yet.");
  }
}