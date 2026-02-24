/*
 * Prueba unitaria para validar el correcto envío de correos electrónicos
 * mediante la clase MailSender.
 * 
 * Este test verifica que el sistema puede enviar correos electrónicos con éxito
 * utilizando las credenciales configuradas en las variables de entorno SMTP.
 * 
 * Se envía un correo de prueba a una dirección de correo válida, con un asunto y cuerpo definidos,
 * y se verifica que el correo se envíe correctamente (retornando 'true' si fue exitoso).
 * 
 * Autor: [Tu nombre]
 * Fecha: [Fecha]
 */

package com.vifac.sys.test;

import com.vifac.sys.util.MailSender;

public class TestMailSender {

    public static void main(String[] args) {
        // Datos de prueba
        String emailDestino = "duvalietabares0919@gmail.com";
        String asunto = "Prueba de envío de correo";
        String cuerpo = "Este es un correo de prueba desde el sistema.";

        // Llamamos al método de envío de correo
        boolean exito = MailSender.enviarCorreo(emailDestino, asunto, cuerpo);

        if (exito) {
            System.out.println("✅ Correo enviado exitosamente.");
        } else {
            System.out.println("❌ No se pudo enviar el correo.");
        }
    }
}

