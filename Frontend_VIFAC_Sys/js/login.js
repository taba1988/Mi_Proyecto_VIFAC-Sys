document.getElementById('login-form').addEventListener('submit', function(e) {
    e.preventDefault(); // Evita la recarga de la página

    const usuarioIngresado = document.getElementById('inputusuario').value;
    const contraseñaIngresada = document.getElementById('inputPassword').value;
    const usuarioValido = "usuarioejemplo"; // Reemplaza con tu usuario válido (solo para este ejemplo)
    const contraseñaValida = "P@sswOrd123!"; // Reemplaza con tu contraseña válida (solo para este ejemplo)

    let usuarioCorrecto = false;
    let contraseñaCorrecta = false;

    // Validación del usuario
    if (usuarioIngresado === usuarioValido) {
        usuarioCorrecto = true;
        document.getElementById('usuario-error').textContent = '';
    } else {
        document.getElementById('usuario-error').textContent = 'Usuario incorrecto.';
    }

    // Validación de la contraseña si el usuario es correcto
    if (usuarioCorrecto) {
        const longitudMinima = 8;
        const tieneMayuscula = /[A-Z]/.test(contraseñaIngresada);
        const tieneMinuscula = /[a-z]/.test(contraseñaIngresada);
        const tieneNumero = /[0-9]/.test(contraseñaIngresada);
        const tieneEspecial = /[!@#$%^&*(),.?":{}|<>]/.test(contraseñaIngresada);

        if (contraseñaIngresada.length >= longitudMinima && tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial && contraseñaIngresada === contraseñaValida) {
            contraseñaCorrecta = true;
            document.getElementById('password-error').textContent = '';
        } else {
            let mensajeError = '';
            if (contraseñaIngresada.length < longitudMinima) {
                mensajeError += 'La contraseña debe tener al menos 8 caracteres. ';
            }
            if (!tieneMayuscula) {
                mensajeError += 'Debe contener al menos una mayúscula. ';
            }
            if (!tieneMinuscula) {
                mensajeError += 'Debe contener al menos una minúscula. ';
            }
            if (!tieneNumero) {
                mensajeError += 'Debe contener al menos un número. ';
            }
            if (!tieneEspecial) {
                mensajeError += 'Debe contener al menos un carácter especial. ';
            }
            if (contraseñaIngresada !== contraseñaValida) {
                mensajeError += 'Contraseña incorrecta.';
            }
            document.getElementById('password-error').textContent = mensajeError.trim();
        }
    } else {
        document.getElementById('password-error').textContent = ''; // Limpiar error de contraseña si usuario es incorrecto
    }

    // Redireccionar si ambas validaciones son exitosas
    if (usuarioCorrecto && contraseñaCorrecta) {
        window.location.href = 'index.html';
    }
});
