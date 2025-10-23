/* ------------------ temporizador.js ------------------ */
/* Temporizador de inactividad: muestra modal al usuario alertando e indicando 
 * que debe inicar sesion nuevamente*/

/* global bootstrap */

const tiempoInactividad = 10 * 60 * 1000; // 10 minutos
let timer;

/* Función para mostrar el modal */
function mostrarModalSesion() {
    const modalElement = document.getElementById('modalSesionExpirada');
    const modal = new bootstrap.Modal(modalElement, {
        backdrop: 'static', // impide cerrar haciendo clic fuera
        keyboard: false     // impide cerrar con ESC
    });
    modal.show();

    /* Botón Aceptar cierra sesión */
    const btnAceptar = modalElement.querySelector('.btn-primary');
    btnAceptar.addEventListener('click', () => {
        window.location.href = "logoutServlet";
    });
}

/* Reinicia el temporizador cada vez que hay actividad */
function reiniciarTemporizador() {
    clearTimeout(timer);
    timer = setTimeout(mostrarModalSesion, tiempoInactividad);
}

/* Eventos que cuentan como actividad del usuario */
['mousemove', 'keydown', 'click', 'scroll', 'touchstart'].forEach(evt => {
    document.addEventListener(evt, reiniciarTemporizador);
});

/* Inicia el temporizador al cargar la página */
reiniciarTemporizador();
