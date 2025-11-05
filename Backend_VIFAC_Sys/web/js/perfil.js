/* global bootstrap, fetch */

function confirmarGuardar() {
    const confirmarModal = new bootstrap.Modal(document.getElementById('confirmarGuardarModal'));
    confirmarModal.show();
}

function guardarCambios() {
    const telefono = document.getElementById('telefono').value;
    const direccion = document.getElementById('direccion').value;

    console.log('Teléfono actualizado:', telefono);
    console.log('Dirección actualizada:', direccion);

    // Enviar datos al servlet
    fetch('PerfilServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `accion=actualizarPerfil&telefono=${encodeURIComponent(telefono)}&direccion=${encodeURIComponent(direccion)}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.estado === 'success') {
            alert(data.mensaje);
            const confirmarModal = bootstrap.Modal.getInstance(document.getElementById('confirmarGuardarModal'));
            confirmarModal.hide();
        } else {
            alert('Error al actualizar perfil: ' + data.mensaje);
        }
    })
    .catch(error => {
        console.error('Error al guardar perfil:', error);
        alert('Error al conectar con el servidor. Intente de nuevo.');
    });
}
