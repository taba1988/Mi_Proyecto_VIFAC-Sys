document.addEventListener("DOMContentLoaded", () => {
    const ayudaVideosContainer = document.getElementById("ayudaVideosContainer");
    const searchInput = document.getElementById("searchInput");
    const paginadorContainer = document.getElementById("paginadorContainer");

    const videosAyudaOriginal = [
        { titulo: "Vista General del sistema", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Configuración inicial del sistema", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Como iniciar una venta", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Como configurar un proveedor", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Como configurar cliente", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Como gestionar usuarios", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Como gestionar contabilidad", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Como gestionar movimientos (archivar, exportar, eliminar)", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Como configurar contraseña", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" },
        { titulo: "Recuperar contraseña", imagenSrc: "img/maxilimpiesa.png", videoUrl: "#" }
    ];

    let currentPage = 1;
    const itemsPerPage = 6;

    function mostrarVideos(videos, page = 1) {
        ayudaVideosContainer.innerHTML = "";
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        const paginaVideos = videos.slice(start, end);

        paginaVideos.forEach(video => {
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

        renderizarPaginador(videos);
    }

    function renderizarPaginador(videos) {
        paginadorContainer.innerHTML = "";
        const totalPages = Math.ceil(videos.length / itemsPerPage);

        if (totalPages <= 1) return; // no mostrar si cabe en 1 página

        const prevBtn = document.createElement("button");
        prevBtn.textContent = "<";
        prevBtn.disabled = currentPage === 1;
        prevBtn.className = "btn btn-sm btn-outline-secondary mx-1";
        prevBtn.onclick = () => {
            if (currentPage > 1) {
                currentPage--;
                mostrarVideos(videos, currentPage);
            }
        };
        paginadorContainer.appendChild(prevBtn);

        for (let i = 1; i <= totalPages; i++) {
            const pageBtn = document.createElement("button");
            pageBtn.textContent = i;
            pageBtn.className = `btn btn-sm mx-1 ${i === currentPage ? "btn-primary" : "btn-outline-secondary"}`;
            pageBtn.onclick = () => {
                currentPage = i;
                mostrarVideos(videos, currentPage);
            };
            paginadorContainer.appendChild(pageBtn);
        }

        const nextBtn = document.createElement("button");
        nextBtn.textContent = ">";
        nextBtn.disabled = currentPage === totalPages;
        nextBtn.className = "btn btn-sm btn-outline-secondary mx-1";
        nextBtn.onclick = () => {
            if (currentPage < totalPages) {
                currentPage++;
                mostrarVideos(videos, currentPage);
            }
        };
        paginadorContainer.appendChild(nextBtn);
    }

    mostrarVideos(videosAyudaOriginal, currentPage);

    window.buscarVideos = function(event) {
        if (event) event.preventDefault();
        const searchTerm = searchInput.value.toLowerCase();
        const resultados = videosAyudaOriginal.filter(video =>
            video.titulo.toLowerCase().includes(searchTerm)
        );
        currentPage = 1;
        mostrarVideos(resultados, currentPage);
    };
});
