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

        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
       })
document.addEventListener('DOMContentLoaded', function () {
  // Datos ejemplo (en producción trae esto desde servidor / API)
  const notificaciones = [
    { icono: "bi-exclamation-triangle-fill text-warning", texto: "Producto 'Detergente' próximo a quedarse sin stock." },
    { icono: "bi-calendar-event-fill text-info", texto: "Próxima visita de proveedor: 05/09/2025." },
    { icono: "bi-x-circle-fill text-danger", texto: "Proveedor XYZ no asistió a la visita programada." }
  ];

  const mensajes = [
    { icono: "bi-cash-stack text-success", texto: "Se realizó una venta por $150.000 (Usuario: Ana)." },
    { icono: "bi-box-seam text-primary", texto: "Inventario actualizado: 20 productos agregados." },
    { icono: "bi-door-open-fill text-danger", texto: "Cierre de caja: $500.000 (Usuario: Carlos – 02/09/2025)." },
    { icono: "bi-door-closed-fill text-primary", texto: "Apertura de caja: $100.000 (Usuario: Ana – 02/09/2025)." }
  ];

  function cargarLista(listaId, datos) {
    const lista = document.getElementById(listaId);
    if (!lista) return;
    lista.innerHTML = "";
    datos.forEach(item => {
      const li = document.createElement("li");
      li.className = "list-group-item";
      li.innerHTML = `<i class="bi ${item.icono} me-2"></i> ${item.texto}`;
      lista.appendChild(li);
    });
  }

  // Actualiza badges con cantidades
  const badgeNot = document.getElementById('badgeNotificaciones');
  const badgeMsg = document.getElementById('badgeMensajes');
  if (badgeNot) badgeNot.textContent = notificaciones.length;
  if (badgeMsg) badgeMsg.textContent = mensajes.length;

  // Al abrir los modales, carga su contenido
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
});
