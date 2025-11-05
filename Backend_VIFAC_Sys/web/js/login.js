document.getElementById('login-form').addEventListener('submit', function (e) {
    const usuario = document.getElementById('inputusuario').value.trim();
    const password = document.getElementById('inputPassword').value.trim();

    let hayErrores = false;
    let mensajeUsuario = '';
    let mensajePassword = '';

    // Validación de usuario vacío
    if (!usuario) {
        mensajeUsuario = 'El usuario es obligatorio.';
        hayErrores = true;
    }

    // Validación de estructura de contraseña
    if (password.length < 8) {
        mensajePassword += 'Debe tener al menos 8 caracteres. ';
        hayErrores = true;
    }
    if (!/[A-Z]/.test(password)) {
        mensajePassword += 'Debe contener al menos una mayúscula. ';
        hayErrores = true;
    }
    if (!/[a-z]/.test(password)) {
        mensajePassword += 'Debe contener al menos una minúscula. ';
        hayErrores = true;
    }
    if (!/\d/.test(password)) {
        mensajePassword += 'Debe contener al menos un número. ';
        hayErrores = true;
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
        mensajePassword += 'Debe contener al menos un carácter especial. ';
        hayErrores = true;
    }

    // Mostrar errores en el DOM
    document.getElementById('usuario-error').textContent = mensajeUsuario;
    document.getElementById('password-error').textContent = mensajePassword;

    // Prevenir envío solo si hay errores de estructura
    if (hayErrores) {
        e.preventDefault(); // Cancela envío si no cumple estructura
    }
});

