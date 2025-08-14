document.addEventListener("DOMContentLoaded", () => {
    const ayudaVideosContainer = document.getElementById("ayudaVideosContainer");
    const searchInput = document.getElementById("searchInput");
    const videosAyudaOriginal = [
        {
            titulo: "Vista General del sistema",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        },
        {
            titulo: "Configuración inicial del sistema",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        },
        {
            titulo: "Como iniciar una venta",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        },
        {
            titulo: "Como configurar un proveedor",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        },
        {
            titulo: "Como configurar cliente",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        },
        {
            titulo: "Como gestionar usuarios",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        },
        {
            titulo: "Como gestionar contabilidad",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        },
        {
            titulo: "Como gestionar movimientos (archivar, exportar, eliminar)",
            imagenSrc: "img/maxilimpiesa.png",
            videoUrl: "#"
        }
        // Agrega más objetos de video aquí
    ];

    function mostrarVideos(videos) {
        ayudaVideosContainer.innerHTML = "";
        videos.forEach(video => {
            const card = document.createElement("div");
            card.className = "col";
            card.innerHTML = `
                <div class="ayuda-card card h-100">
                    <img src="${video.imagenSrc}" class="card-img-top" alt="${video.titulo}">
                    <div class="card-body-ayuda">
                        <h5 class="card-title-ayuda">${video.titulo}</h5>
                        <a href="${video.videoUrl}" class="btn btn-outline-primary btn-sm">Ver Video</a>
                    </div>
                </div>
            `;
            ayudaVideosContainer.appendChild(card);
        });
    }

    mostrarVideos(videosAyudaOriginal);

    window.buscarVideos = function(event) {
        if (event) {
            event.preventDefault();
        }
        const searchTerm = searchInput.value.toLowerCase();
        const resultados = videosAyudaOriginal.filter(video =>
            video.titulo.toLowerCase().includes(searchTerm)
        );
        mostrarVideos(resultados);
    };
});