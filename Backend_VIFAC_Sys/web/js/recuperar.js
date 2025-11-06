/* global bootstrap */

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
                const modalBody = document.querySelector('#confirmChangePasswordModal .modal-body');
                modalBody.textContent = data.message;
                const confirmModal = new bootstrap.Modal(document.getElementById('confirmChangePasswordModal'));
                confirmModal.show();

                if (data.status === 'success') {
                    window.location.href = 'login.jsp'; // redirige al login
                }
            } catch (err) {
                console.error(err);
                alert('Error al procesar la solicitud.');
            }
        });
    }

    // ---- VALIDACIÓN EN TIEMPO REAL DE CONTRASEÑAS ----
    const nueva = document.querySelector('input[name="nuevaContrasena"]');
    const confirmar = document.querySelector('input[name="confirmarContrasena"]');
    const error = document.getElementById('password-match-error');

    if (nueva && confirmar && error) {
        function validarCoincidencia() {
            const pass1 = nueva.value.trim();
            const pass2 = confirmar.value.trim();

            if (!pass2) {
                error.classList.add('d-none');
                confirmar.classList.remove('is-invalid', 'is-valid');
                return;
            }

            if (pass1 === pass2) {
                error.textContent = "️";
                error.classList.remove('d-none', 'text-danger');
                error.classList.add('text-success');
                confirmar.classList.remove('is-invalid');
                confirmar.classList.add('is-valid');
            } else {
                error.textContent = "Las contraseñas no coinciden ❌";
                error.classList.remove('d-none', 'text-success');
                error.classList.add('text-danger');
                confirmar.classList.remove('is-valid');
                confirmar.classList.add('is-invalid');
            }
        }

        nueva.addEventListener('input', validarCoincidencia);
        confirmar.addEventListener('input', validarCoincidencia);
    }

});
