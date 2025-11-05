document.addEventListener('DOMContentLoaded', function() {

    // ---- FORMULARIO RESTABLECER CONTRASEÑA ----
    const resetForm = document.getElementById('change-password-form');
    if (resetForm) {
        resetForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const token = document.querySelector('input[name="token"]').value;
            const nueva = document.getElementById('inputNewPassword').value.trim();
            const confirmar = document.getElementById('inputConfirmNewPassword').value.trim();

            if (!nueva || !confirmar) { alert('Complete ambos campos'); return; }
            if (nueva !== confirmar) { alert('Las contraseñas no coinciden'); return; }

            try {
                const response = await fetch('RestablecerContrasenaServlet', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `token=${encodeURIComponent(token)}&nuevaContrasena=${encodeURIComponent(nueva)}&confirmarContrasena=${encodeURIComponent(confirmar)}`
                });
                const data = await response.json();
                alert(data.message);

                if (data.status === 'success') {
                    window.location.href = 'login.jsp'; // redirige al login
                }
            } catch (err) {
                console.error(err);
                alert('Error al procesar la solicitud.');
            }
        });
    }
  
});
