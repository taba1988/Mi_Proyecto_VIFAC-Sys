document.addEventListener('DOMContentLoaded', function() {
    const botonEnviar = document.querySelector('.buttons-container .btn-outline-success');
    if (botonEnviar) {
        botonEnviar.addEventListener('click', enviarFacturaPorCorreo);
    }

    const botonImprimir = document.querySelector('.buttons-container .btn-outline-secondary');
    if (botonImprimir) {
        botonImprimir.addEventListener('click', imprimirFactura); // Cambiado a la nueva función
    }
});

function enviarFacturaPorCorreo() {
    const emailClienteSpan = document.getElementById('emailCliente');
    if (!emailClienteSpan || !emailClienteSpan.textContent.includes('@')) {
        alert('No se encontró o no es válido el correo electrónico del cliente.');
        return;
    }
    const emailCliente = emailClienteSpan.textContent;

    const facturaHTML = document.documentElement.outerHTML;

    fetch('/enviar-factura', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: emailCliente,
            factura: facturaHTML,
        }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('La factura se envió correctamente al correo electrónico del cliente.');
        } else {
            alert('Hubo un error al enviar la factura: ' + data.error);
        }
    })
    .catch(error => {
        console.error('Error al enviar la factura:', error);
        alert('Ocurrió un error inesperado al intentar enviar la factura.');
    });
}

function imprimirFactura() {
    window.print(); // Esta línea activa la función de impresión del navegador
}