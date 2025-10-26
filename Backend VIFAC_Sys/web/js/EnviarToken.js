document.addEventListener('DOMContentLoaded', function() {

    // ---- FORMULARIO ENVIAR TOKEN ----
    const tokenForm = document.getElementById('password-reset-form');
    if (tokenForm) {
        tokenForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const emailInput = document.getElementById('inputEmail');
            if (!emailInput) { alert('Campo de correo no existe'); return; }
            const email = emailInput.value.trim();
            if (!email) { alert('Debe ingresar un correo válido'); return; }

            try {
                const response = await fetch('EnviarTokenRecuperacionServlet', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `email=${encodeURIComponent(email)}`
                });
                const data = await response.json();
                alert(data.message);
            } catch (err) {
                console.error(err);
                alert('Error al enviar la solicitud');
            }
        });
    }
});
