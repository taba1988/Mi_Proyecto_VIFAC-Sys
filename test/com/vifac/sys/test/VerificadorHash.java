package com.vifac.sys.test;

import org.mindrot.jbcrypt.BCrypt;

public class VerificadorHash {
    public static void main(String[] args) {
        String passwordPlano = "Sagitario1988."; // La contraseña que quieres verificar
        String hashBD = "$2a$10$dqu9sbHmAyhzBoSdDt1W6ecIcIfN4uue0.tLI/GTfiwagsg1xK.c6"; // El hash guardado en la BD

        if (BCrypt.checkpw(passwordPlano, hashBD)) {
            System.out.println("La contraseña es correcta");
        } else {
            System.out.println("La contraseña es incorrecta");
        }
    }
}

