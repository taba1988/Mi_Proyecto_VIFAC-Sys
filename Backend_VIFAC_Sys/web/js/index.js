/* global bootstrap */

document.addEventListener('DOMContentLoaded', function() {
    // Selecciona el botón y el offcanvas
    const offcanvasButton = document.querySelector('.offcanvas-button');
    const myOffcanvas = document.getElementById('offcanvas');

    // Verifica que los elementos existan antes de agregar los listeners
    if (myOffcanvas && offcanvasButton) {
        // Oculta el botón cuando el offcanvas se muestra
        myOffcanvas.addEventListener('show.bs.offcanvas', function () {
            offcanvasButton.classList.add('d-none');
        });

        // Muestra el botón cuando el offcanvas se oculta
        myOffcanvas.addEventListener('hidden.bs.offcanvas', function () {
            offcanvasButton.classList.remove('d-none');
        });
    }
});

var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
});

document.addEventListener('DOMContentLoaded', function () {

    let notificaciones = [];
    let mensajes = [];

    async function cargarDesdeServidor() {
        try {
            const response = await fetch('NotificacionServlet');
            if (!response.ok) return;

            const data = await response.json();

            notificaciones = data.filter(n => !n.leido);
            mensajes = data.filter(n => n.leido);

            const badgeNot = document.getElementById('badgeNotificaciones');
            const badgeMsg = document.getElementById('badgeMensajes');

            if (badgeNot) badgeNot.textContent = notificaciones.length;
            if (badgeMsg) badgeMsg.textContent = mensajes.length;

        } catch (e) {
            console.error('Error cargando notificaciones', e);
        }
    }
   
    // PINTAR LISTAS
    function cargarLista(listaId, datos) {
        const lista = document.getElementById(listaId);
        if (!lista) return;

        lista.innerHTML = "";

        datos.forEach(item => {
            const li = document.createElement("li");
            li.className = "list-group-item";

            li.innerHTML = `
                <i class="bi bi-bell-fill me-2"></i>
                ${item.mensaje}
                ${!item.leido ? `
                    <button class="btn btn-sm btn-outline-success float-end"
                        onclick="marcarLeida(${item.idNotificacion})">✓</button>
                ` : ''}
            `;
            lista.appendChild(li);
        });
    }

    // MARCAR COMO LEÍDA
    window.marcarLeida = async function(idNotificacion) {
        await fetch('NotificacionServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                accion: 'marcarLeida',
                idNotificacion: idNotificacion
            })
        });

        cargarDesdeServidor();
    };
    
    // EVENTOS MODALES
    const modalNot = document.getElementById('modalNotificaciones');
    if (modalNot) {
        modalNot.addEventListener('show.bs.modal', function () {
            cargarLista('listaNotificaciones', notificaciones);
        });
    }

    const modalMsg = document.getElementById('modalMensajes');
    if (modalMsg) {
        modalMsg.addEventListener('show.bs.modal', function () {
            cargarLista('listaMensajes', mensajes);
        });
    }
    
    cargarDesdeServidor();
  }
);
