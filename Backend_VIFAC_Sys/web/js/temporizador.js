/* ------------------ temporizador.js ------------------ */
/* global bootstrap */

// 1. Usamos 'var' en lugar de 'const' porque 'var' permite ser redeclarada.
// 2. Envolvemos en un check para que solo se defina si no existe ya.
if (typeof tiempoInactividad === 'undefined') {
    var tiempoInactividad = 10 * 60 * 1000; // 10 minutos
}

// Usamos 'var' para el timer por la misma razón de compatibilidad
var timer;

function mostrarModalSesion() {
    var modalElement = document.getElementById('modalSesionExpirada');
    if (modalElement) {
        var modal = new bootstrap.Modal(modalElement, {
            backdrop: 'static',
            keyboard: false
        });
        modal.show();
    }
}

function reiniciarTemporizador() {
    if (typeof timer !== 'undefined') {
        clearTimeout(timer);
    }
    timer = setTimeout(mostrarModalSesion, tiempoInactividad);
}

// Eventos de actividad
['mousemove', 'keydown', 'click', 'scroll', 'touchstart'].forEach(function(evt) {
    document.addEventListener(evt, reiniciarTemporizador);
});

// Inicio inicial
reiniciarTemporizador();