document.getElementById('password-reset-form').addEventListener('submit', function(e) {
    e.preventDefault(); // Evita la recarga de la página
    // Aquí deberías agregar la lógica para enviar el correo de restablecimiento de contraseña
    alert('Se ha enviado un enlace de restablecimiento a su correo electrónico (funcionalidad no implementada).');
    // O podrías redirigir a una página de confirmación:
    // window.location.href = 'password-reset-sent.html';
});
