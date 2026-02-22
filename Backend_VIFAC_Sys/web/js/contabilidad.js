/* global bootstrap */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Buscamos los enlaces por su atributo href
    const enlaces = document.querySelectorAll('a[href="ContabilidadServlet"], a[href="MovimientosServlet"]');

    enlaces.forEach(enlace => {
        enlace.addEventListener("click", function (e) {
            // 2. IMPORTANTE: Evita que el navegador abra el JSON en pantalla completa
            e.preventDefault(); 

            const url = this.getAttribute("href");

            // 3. Petición al Servlet por debajo (AJAX)
            fetch(url)
                .then(response => response.json())
                .then(data => {
                    // 4. Muestra el JSON en la consola (F12) como querías
                    console.log("Respuesta del servidor para " + url + ":", data);

                    // 5. Busca y muestra el modal
                    const modalElement = document.getElementById("modalDesarrollo");
                    if (modalElement) {
                        const instance = bootstrap.Modal.getOrCreateInstance(modalElement);
                        instance.show();
                    }
                })
                .catch(error => console.error("Error en la petición:", error));
        });
    });
});