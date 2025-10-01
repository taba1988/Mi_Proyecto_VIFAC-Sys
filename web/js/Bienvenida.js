
/* global bootstrap */

document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get("mensajeBienvenida") === "true") {
        const modal = new bootstrap.Modal(document.getElementById("modalBienvenida"));
        modal.show();
    }
});

