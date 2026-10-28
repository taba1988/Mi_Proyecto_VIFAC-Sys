
/**
 * loading.js
 * Control automático del overlay de carga de VIFAC-Sys.
 * Autor: Orlanduvalie Tabares Gutierrez
 */

const Loading = (function () {

    function mostrar(texto, elemento) {
        const overlay = document.getElementById("loadingOverlay");
        const textoEl = document.getElementById("loadingTexto");

        if (!overlay) return;

        if (texto && textoEl) {
            textoEl.childNodes[0].textContent = texto;
        }

        const objetivo = elemento instanceof HTMLElement
            ? elemento.closest("form") || elemento
            : null;

        if (objetivo) {
            const rect = objetivo.getBoundingClientRect();

            overlay.style.setProperty(
                "--loading-top",
                (rect.top + rect.height / 2) + "px"
            );

            overlay.style.setProperty(
                "--loading-left",
                (rect.left + rect.width / 2) + "px"
            );
        } else {
            overlay.style.setProperty("--loading-top", "50%");
            overlay.style.setProperty("--loading-left", "50%");
        }

        overlay.classList.add("activo");
    }

    function ocultar() {
        const overlay = document.getElementById("loadingOverlay");

        if (overlay) {
            overlay.classList.remove("activo");
        }
    }

    function inicializar() {
        document.querySelectorAll("form").forEach(function (form) {
            if (form.dataset.loadingInicializado) return;
            form.dataset.loadingInicializado = "true";

            form.addEventListener("submit", function (evento) {
                if (evento.defaultPrevented || !form.checkValidity()) return;

                mostrar("Procesando", form);
            });
        });

        document.querySelectorAll("a[href]").forEach(function (enlace) {
            if (enlace.dataset.loadingInicializado) return;
            enlace.dataset.loadingInicializado = "true";

            enlace.addEventListener("click", function (evento) {
                const href = enlace.getAttribute("href");

                if (
                    evento.defaultPrevented ||
                    evento.ctrlKey || evento.metaKey ||
                    evento.shiftKey || evento.altKey ||
                    enlace.target === "_blank" ||
                    enlace.hasAttribute("download") ||
                    !href || href.startsWith("#") ||
                    href.startsWith("javascript:")
                ) return;

                mostrar("Cargando");
            });
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", inicializar);
    } else {
        inicializar();
    }

    return { mostrar, ocultar };

})();
