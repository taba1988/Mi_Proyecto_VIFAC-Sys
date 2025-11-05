package com.vifac.sys.test;

import org.mindrot.jbcrypt.BCrypt;

public class VerificadorHash {
    public static void main(String[] args) {
        String passwordPlano = "Familiat14$&"; // La contraseña que quieres verificar
        String hashBD = "$2a$10$b9ytcokhlAaAFULqonKWkOVDJOzzWRNuAawzuJUn7s5LIkESQSOvK"; // El hash guardado en la BD

        if (BCrypt.checkpw(passwordPlano, hashBD)) {
            System.out.println("La contraseña es correcta");
        } else {
            System.out.println("La contraseña es incorrecta");
        }
    }
}

