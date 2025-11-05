package com.vifac.sys.test;

import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.Usuario;
import java.util.List;

public class TestUsuarioDAO {

    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Crear un usuario de prueba
        Usuario usuarioPrueba = new Usuario();
        usuarioPrueba.setNombre("Test User2");
        usuarioPrueba.setDocumento("12345670");
        usuarioPrueba.setTelefono("555-1235");
        usuarioPrueba.setEmail("testuser2@example.com");
        usuarioPrueba.setNombreUsuario("tester2");
        usuarioPrueba.setContrasena("test123");
        usuarioPrueba.setCargo("Administrador");
        usuarioPrueba.setIdRol(1);
        usuarioPrueba.setEstado("Activo");
        usuarioPrueba.setIntentosFallidos(0);

        // Insertar usuario
        boolean agregado = usuarioDAO.agregarUsuario(usuarioPrueba);
        System.out.println("¿Usuario agregado? " + agregado);

        // Listar usuarios y mostrarlos
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();
        System.out.println("Listado de usuarios en BD:");
        for (Usuario u : usuarios) {
            System.out.println("ID: " + u.getIdUsuario() + ", Nombre: " + u.getNombre() + ", Usuario: " + u.getNombreUsuario());
        }
    }
}
