/* global bootstrap, fetch */

/**
 * Muestra el modal de confirmación antes de guardar cambios
 */
function confirmarGuardar() {
    const confirmarModal = new bootstrap.Modal(document.getElementById('confirmarGuardarModal'));
    confirmarModal.show();
}

function guardarCambios() {
    const telefono = document.getElementById('telefono').value;
    const direccion = document.getElementById('direccion').value;

    console.log('Teléfono actualizado:', telefono);
    console.log('Dirección actualizada:', direccion);

    // Construir los datos para POST
    const formData = new URLSearchParams();
    formData.append('accion', 'actualizarPerfil');
    formData.append('telefono', telefono);
    formData.append('direccion', direccion);

    // Enviar datos al servlet
    fetch("PerfilServlet", {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
    })
    .then(response => {
        console.log('Respuesta cruda del servidor:', response);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('JSON recibido del servidor:', data);

        // Cerrar modal de confirmar
        const confirmarModal = bootstrap.Modal.getInstance(
            document.getElementById('confirmarGuardarModal')
        );
        if (confirmarModal) confirmarModal.hide();

        // Preparar modalMensaje
        const modalTexto = document.getElementById("modalMensajeTexto");
        const modal = new bootstrap.Modal(document.getElementById("modalMensaje"));

        if (data.status === 'success') {
            modalTexto.innerText = data.message; 
            modal.show();
        } else {
            modalTexto.innerText = "Error al actualizar perfil: " + (data.message || 'desconocido');
            modal.show();
        }
    })
    .catch(error => {
        console.error('Error al guardar perfil:', error);

        const modalTexto = document.getElementById("modalMensajeTexto");
        const modal = new bootstrap.Modal(document.getElementById("modalMensaje"));
        modalTexto.innerText = "Error al conectar con el servidor. Intente de nuevo.";
        modal.show();
    });
}
