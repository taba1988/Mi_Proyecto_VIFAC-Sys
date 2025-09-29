package com.vifac.sys.test;

import org.mindrot.jbcrypt.BCrypt;

public class GeneradorHash {
    public static void main(String[] args) {
        String contrasenaTextoPlano = "Familiat14#"; 
        String contrasenaHasheada = BCrypt.hashpw(contrasenaTextoPlano, BCrypt.gensalt());
        System.out.println(contrasenaHasheada);
    }
}