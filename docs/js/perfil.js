function confirmarGuardar() {
    const confirmarModal = new bootstrap.Modal(document.getElementById('confirmarGuardarModal'));
    confirmarModal.show();
}

function guardarCambios() {
    // Aquí deberías agregar la lógica para enviar los datos actualizados al servidor
    const telefono = document.getElementById('telefono').value;
    const direccion = document.getElementById('direccion').value;
    console.log('Teléfono actualizado:', telefono);
    console.log('Dirección actualizada:', direccion);
    alert('Perfil actualizado exitosamente (funcionalidad de guardado no implementada).');
    // Puedes redirigir a otra página después de guardar, por ejemplo:
    // window.location.href = 'dashboard.html';

    // Cerrar el modal después de "guardar"
    const confirmarModal = bootstrap.Modal.getInstance(document.getElementById('confirmarGuardarModal'));
    confirmarModal.hide();
}
