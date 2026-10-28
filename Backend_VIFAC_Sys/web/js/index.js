
/* global bootstrap */

document.addEventListener('DOMContentLoaded', function () {

    // =====================================================
    // MENÚ LATERAL
    // =====================================================

    const offcanvasButton = document.querySelector('.offcanvas-button');
    const myOffcanvas = document.getElementById('offcanvas');

    if (myOffcanvas && offcanvasButton) {
        myOffcanvas.addEventListener('show.bs.offcanvas', function () {
            offcanvasButton.classList.add('d-none');
        });

        myOffcanvas.addEventListener('hidden.bs.offcanvas', function () {
            offcanvasButton.classList.remove('d-none');
        });
    }

    // =====================================================
    // TOOLTIPS
    // =====================================================

    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function (elemento) {
        new bootstrap.Tooltip(elemento);
    });

    // =====================================================
    // VARIABLES
    // =====================================================

    let notificaciones = [];
    let mensajes = [];
    let eliminados = [];
    let bandejaActual = 'RECIBIDOS';
    let mensajeActual = null;
    let origenMensaje = null;
    let accionDetalle = null;

    const modalNot = document.getElementById('modalNotificaciones');
    const modalMsg = document.getElementById('modalMensajes');

    // =====================================================
    // CARGAR NOTIFICACIONES DESDE EL SERVIDOR
    // =====================================================

    async function cargarDesdeServidor() {
        try {
            const response = await fetch('NotificacionServlet');

            if (!response.ok) {
                throw new Error('No fue posible consultar las notificaciones.');
            }

            const data = await response.json();

            notificaciones = data.filter(function (n) {
                return !n.leido && !n.eliminado;
            });

            mensajes = data.filter(function (n) {
                return n.leido && !n.eliminado;
            });

            eliminados = data.filter(function (n) {
                return n.eliminado;
            });

            actualizarContadores();
            cargarLista('listaNotificaciones', notificaciones);
            cargarLista('listaMensajes', obtenerMensajesBandeja());

        } catch (error) {
            console.error('Error cargando notificaciones:', error);
        }
    }

    function actualizarContadores() {
        const badgeNot = document.getElementById('badgeNotificaciones');
        const badgeMsg = document.getElementById('badgeMensajes');

        if (badgeNot) {
            badgeNot.textContent = notificaciones.length;
            badgeNot.classList.toggle('d-none', notificaciones.length === 0);
        }

        if (badgeMsg) {
            badgeMsg.textContent = mensajes.length;
            badgeMsg.classList.toggle('d-none', mensajes.length === 0);
        }
    }

    // =====================================================
    // BANDEJAS DE MENSAJES
    // =====================================================

    function obtenerMensajesBandeja() {
        switch (bandejaActual) {
            case 'LEIDOS':
                return mensajes;

            case 'ELIMINADOS':
                return eliminados;

            case 'RECIBIDOS':
            default:
                return notificaciones;
        }
    }

    function prepararBandejas() {
        if (!modalMsg) {
            return;
        }

        const cuerpo = modalMsg.querySelector('.modal-body');
        const lista = document.getElementById('listaMensajes');

        if (!cuerpo || !lista) {
            return;
        }

        let navegacion = document.getElementById('navegacionBandejas');

        if (!navegacion) {
            navegacion = document.createElement('div');
            navegacion.id = 'navegacionBandejas';
            navegacion.className = 'd-flex flex-wrap gap-2 mb-3';

            const bandejas = [
                { id: 'RECIBIDOS', texto: 'Recibidos' },
                { id: 'LEIDOS', texto: 'Leídos' },
                { id: 'ELIMINADOS', texto: 'Eliminados' }
            ];

            bandejas.forEach(function (bandeja) {
                const boton = document.createElement('button');

                boton.type = 'button';
                const colores = {
                RECIBIDOS: 'btn-outline-primary',
                LEIDOS: 'btn-outline-success',
                ELIMINADOS: 'btn-outline-danger'
            };

            boton.className = `btn btn-sm ${colores[bandeja.id]} flex-fill`;
                boton.dataset.bandeja = bandeja.id;
                boton.textContent = bandeja.texto;

                boton.addEventListener('click', function () {
                    bandejaActual = bandeja.id;
                    actualizarBotonesBandeja();
                    cargarLista('listaMensajes', obtenerMensajesBandeja());
                });

                navegacion.appendChild(boton);
            });

            cuerpo.insertBefore(navegacion, lista);
        }

        actualizarBotonesBandeja();
    }

    function actualizarBotonesBandeja() {
        const navegacion = document.getElementById('navegacionBandejas');

        if (!navegacion) {
            return;
        }

        navegacion.querySelectorAll('button[data-bandeja]').forEach(function (boton) {
            const seleccionada = boton.dataset.bandeja === bandejaActual;

            boton.classList.toggle('active', seleccionada);
            boton.setAttribute('aria-pressed', seleccionada ? 'true' : 'false');
        });
    }

    // =====================================================
    // PINTAR LISTAS
    // =====================================================

    function cargarLista(listaId, datos) {
        const lista = document.getElementById(listaId);

        if (!lista) {
            return;
        }

        lista.replaceChildren();

        if (!datos || datos.length === 0) {
            const liVacio = document.createElement('li');
            liVacio.className = 'list-group-item text-muted text-center py-3';

            if (listaId === 'listaNotificaciones') {
                liVacio.textContent = 'No tienes notificaciones pendientes.';
            } else if (bandejaActual === 'ELIMINADOS') {
                liVacio.textContent = 'La bandeja de eliminados está vacía.';
            } else if (bandejaActual === 'LEIDOS') {
                liVacio.textContent = 'No tienes mensajes leídos.';
            } else {
                liVacio.textContent = 'No tienes mensajes recibidos.';
            }

            lista.appendChild(liVacio);
            return;
        }

        datos.forEach(function (item) {
            const li = document.createElement('li');

            li.className = 'list-group-item d-flex align-items-start gap-2';
            li.style.cursor = 'pointer';

            const icono = document.createElement('i');
            icono.className = 'bi bi-envelope-fill text-primary mt-1';

            const contenido = document.createElement('div');
            contenido.className = 'flex-grow-1';

            const texto = document.createElement('div');
            texto.textContent = item.mensaje || '(Mensaje sin contenido)';
            texto.className = 'text-break';

            const fecha = document.createElement('small');
            fecha.className = 'text-muted d-block mt-1';
            fecha.textContent = item.fechaCreacion
                ? new Date(item.fechaCreacion).toLocaleString()
                : '';

            contenido.appendChild(texto);
            contenido.appendChild(fecha);

            li.appendChild(icono);
            li.appendChild(contenido);

li.addEventListener('click', function () {
    const esNotificacion = listaId === 'listaNotificaciones';

    const origen = esNotificacion ? 'RECIBIDOS' : bandejaActual;
    const modalOrigen = esNotificacion ? modalNot : modalMsg;

    abrirMensaje(item, origen, modalOrigen);
});

            lista.appendChild(li);
        });
    }

    // =====================================================
    // VENTANA INDEPENDIENTE PARA LEER MENSAJES
    // =====================================================

    function prepararModalDetalle() {
        let modal = document.getElementById('modalDetalleNotificacion');

        if (modal) {
            return modal;
        }

        modal = document.createElement('div');
        modal.id = 'modalDetalleNotificacion';
        modal.className = 'modal fade';
        modal.tabIndex = -1;
        modal.setAttribute('aria-hidden', 'true');

        modal.innerHTML = `
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Mensaje</h5>
                        <button type="button" class="btn-close"
                            data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <small id="detalleFecha" class="text-muted d-block mb-3"></small>
                        <div id="detalleContenido" class="text-break"
                            style="white-space: pre-wrap;"></div>
                    </div>
                    <div class="modal-footer flex-wrap">
                        <button type="button" id="btnRestaurarMensaje"
                            class="btn btn-outline-primary d-none">
                            Restaurar
                        </button>
                        <button type="button" id="btnEliminarDefinitivo"
                            class="btn btn-danger d-none">
                            Eliminar definitivamente
                        </button>
                        <button type="button" id="btnEliminarMensaje"
                            class="btn btn-outline-danger">
                            Eliminar
                        </button>
                        <button type="button" class="btn btn-secondary"
                            data-bs-dismiss="modal">
                            Cerrar
                        </button>
                    </div>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        modal.querySelector('#btnEliminarMensaje').addEventListener('click', async function () {
            if (!mensajeActual) {
                return;
            }

            const correcto = await ejecutarAccion(
                'eliminar',
                mensajeActual.idNotificacion
            );

            if (correcto) {
                accionDetalle = 'eliminar';
                bootstrap.Modal.getOrCreateInstance(modal).hide();
            }
        });

        modal.querySelector('#btnRestaurarMensaje').addEventListener('click', async function () {
            if (!mensajeActual) {
                return;
            }

            const correcto = await ejecutarAccion(
                'restaurar',
                mensajeActual.idNotificacion
            );

            if (correcto) {
                accionDetalle = 'restaurar';
                bootstrap.Modal.getOrCreateInstance(modal).hide();
            }
        });

        modal.querySelector('#btnEliminarDefinitivo').addEventListener('click', async function () {
            if (!mensajeActual) {
                return;
            }

            const confirmar = window.confirm(
                '¿Eliminar definitivamente este mensaje? Esta acción no se puede deshacer.'
            );

            if (!confirmar) {
                return;
            }

            const correcto = await ejecutarAccion(
                'eliminarDefinitivamente',
                mensajeActual.idNotificacion
            );

            if (correcto) {
                accionDetalle = 'eliminarDefinitivamente';
                bootstrap.Modal.getOrCreateInstance(modal).hide();
            }
        });

           modal.addEventListener('hidden.bs.modal', async function () {
               const mensaje = mensajeActual;
               const origen = origenMensaje;
               const accion = accionDetalle;

               mensajeActual = null;
               origenMensaje = null;
               accionDetalle = null;
           
               if (mensaje && origen === 'RECIBIDOS' && !accion) {
                   await ejecutarAccion('marcarLeida', mensaje.idNotificacion);
               } else {
                   await cargarDesdeServidor();
               }
           
               document.querySelectorAll('.modal-backdrop').forEach(function (fondo) {
                   fondo.remove();
               });

               document.body.classList.remove('modal-open');
               document.body.style.removeProperty('overflow');
               document.body.style.removeProperty('padding-right');
           
               document.querySelectorAll('.modal').forEach(function (ventana) {
                   if (!ventana.classList.contains('show')) {
                       ventana.style.removeProperty('display');
                       ventana.setAttribute('aria-hidden', 'true');
                   }
               });
           });

        return modal;
    }

    function abrirMensaje(item, origen, modalOrigen) {
        mensajeActual = item;
        origenMensaje = origen;
        accionDetalle = null;

        const modalDetalle = prepararModalDetalle();

        modalDetalle.querySelector('.modal-title').textContent =
            item.tipo || 'Mensaje';

        modalDetalle.querySelector('#detalleContenido').textContent =
            item.mensaje || '(Mensaje sin contenido)';

        modalDetalle.querySelector('#detalleFecha').textContent =
            item.fechaCreacion
                ? 'Fecha: ' + new Date(item.fechaCreacion).toLocaleString()
                : '';

        const botonEliminar = modalDetalle.querySelector('#btnEliminarMensaje');
        const botonRestaurar = modalDetalle.querySelector('#btnRestaurarMensaje');
        const botonDefinitivo = modalDetalle.querySelector('#btnEliminarDefinitivo');

        botonEliminar.classList.toggle('d-none', origen === 'ELIMINADOS');
        botonRestaurar.classList.toggle('d-none', origen !== 'ELIMINADOS');
        botonDefinitivo.classList.toggle('d-none', origen !== 'ELIMINADOS');

        if (modalOrigen && modalOrigen.classList.contains('show')) {
            modalOrigen.addEventListener('hidden.bs.modal', function mostrarDetalle() {
                modalOrigen.removeEventListener('hidden.bs.modal', mostrarDetalle);
                bootstrap.Modal.getOrCreateInstance(modalDetalle).show();
            }, { once: true });

            bootstrap.Modal.getOrCreateInstance(modalOrigen).hide();
        } else {
            bootstrap.Modal.getOrCreateInstance(modalDetalle).show();
        }
    }

    // =====================================================
    // ACCIONES CONTRA EL SERVLET
    // =====================================================

    async function ejecutarAccion(accion, idNotificacion) {
        try {
            const response = await fetch('NotificacionServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    accion: accion,
                    idNotificacion: idNotificacion
                })
            });

            const resultado = await response.json();

            if (!response.ok || resultado.status !== 'ok') {
                window.alert(resultado.message || 'No se pudo completar la operación.');
                return false;
            }

            await cargarDesdeServidor();

            return true;

        } catch (error) {
            console.error('Error procesando notificación:', error);
            window.alert('Ocurrió un error al comunicarse con el servidor.');
            return false;
        }
    }

    // Compatibilidad con el botón anterior, si se usa en otra parte del proyecto.
    window.marcarLeida = async function (idNotificacion) {
        await ejecutarAccion('marcarLeida', idNotificacion);
    };

    // =====================================================
    // EVENTOS DE LAS VENTANAS EXISTENTES
    // =====================================================

    if (modalNot) {
        modalNot.addEventListener('show.bs.modal', function () {
            cargarLista('listaNotificaciones', notificaciones);
        });
    }

    if (modalMsg) {
        prepararBandejas();

        modalMsg.addEventListener('show.bs.modal', function () {
            actualizarBotonesBandeja();
            cargarLista('listaMensajes', obtenerMensajesBandeja());
        });
    }

    // Carga inicial
    cargarDesdeServidor();

});
