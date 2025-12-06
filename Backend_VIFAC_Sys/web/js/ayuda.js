/* global bootstrap */

document.addEventListener("DOMContentLoaded", () => {
    const ayudaVideosContainer = document.getElementById("ayudaVideosContainer");
    const searchInput = document.getElementById("busquedaAyuda");
    const paginadorContainer = document.getElementById("paginadorContainer");
    let videosAyudaOriginal = [];
    let currentPage = 1;
    const itemsPerPage = 6;

    // Lista de módulos o títulos de videos por defecto
    const titulosPorDefecto = [
        "Vista General del sistema",
        "Configuración inicial del sistema",
        "Como iniciar una venta",
        "Como configurar un proveedor",
        "Como configurar cliente",
        "Como gestionar usuarios",
        "Como gestionar contabilidad",
        "Como gestionar movimientos",
        "Como cambiar contraseña",
        "Recuperar contraseña"
    ];

    async function cargarVideos() {
        // Llamada al servlet
        let data = [];
        try {
            const resp = await fetch("AyudaServlet");
            data = await resp.json();
            console.log("Datos recibidos de AyudaServlet:", data);
        } catch (err) {
            console.warn("No se pudo cargar desde el servidor, se usan tarjetas por defecto");
        }

        // Si la BD no devuelve datos, generamos tarjetas vacías con imagen por defecto
        if (!Array.isArray(data) || data.length === 0) {
            videosAyudaOriginal = titulosPorDefecto.map(titulo => ({
                titulo: "",
                urlVideo: "#",
                url_imagen: null,
                nombreDefecto: titulo // para identificar el propósito de la tarjeta
            }));
        } else {
            // Los datos reales del servidor
            videosAyudaOriginal = data.map(v => ({
                id: v.id,
                titulo: v.titulo || "",
                urlVideo: v.url_video || "#",
                url_imagen: v.url_imagen || null,
                nombreDefecto: v.titulo // o el nombre que identifique el video
            }));
        }

        mostrarVideos(videosAyudaOriginal, currentPage);
    }

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
                          <img src="img/Empresa.png" class="card-img-top" alt="${video.nombreDefecto}">
                          <div class="card-body-ayuda">
                              <h5 class="card-title-ayuda">${video.titulo || video.nombreDefecto}</h5>
                              <a href="${video.urlVideo}" target="_blank" class="btn btn-outline-primary btn-sm">
                                  <i class="bi bi-play-fill"></i>
                              </a>
                              ${idRol === 1 ? `
                              <button class="btn btn-outline-success btn-sm" data-bs-toggle="modal" data-bs-target="#modalCargarVideo"
                                  onclick="
                                      document.getElementById('tituloVideo').value='${video.titulo}';
                                      document.getElementById('urlVideo').value='${video.urlVideo}';
                                      document.getElementById('moduloSeleccionado').value='${video.nombreDefecto}';
                                  ">
                                  <i class="bi bi-upload"></i>
                              </button>
                              <button class="btn btn-outline-danger btn-sm" onclick="eliminarVideo(${video.id})">
                              <i class="bi bi-trash"></i>
                              </button>
                              ` : ''}
                          </div>
                      </div>
`                  ;
                ayudaVideosContainer.appendChild(card);
            });
        renderizarPaginador(videos);
    }

    function renderizarPaginador(videos) {
        paginadorContainer.innerHTML = "";
        const totalPages = Math.ceil(videos.length / itemsPerPage);
        if (totalPages <= 1) return;

        const prevBtn = document.createElement("button");
        prevBtn.textContent = "<";
        prevBtn.disabled = currentPage === 1;
        prevBtn.className = "btn btn-sm btn-outline-secondary mx-1";
        prevBtn.onclick = () => { if (currentPage > 1) { currentPage--; mostrarVideos(videos, currentPage); } };
        paginadorContainer.appendChild(prevBtn);

        for (let i = 1; i <= totalPages; i++) {
            const pageBtn = document.createElement("button");
            pageBtn.textContent = i;
            pageBtn.className = `btn btn-sm mx-1 ${i === currentPage ? "btn-primary" : "btn-outline-secondary"}`;
            pageBtn.onclick = () => { currentPage = i; mostrarVideos(videos, currentPage); };
            paginadorContainer.appendChild(pageBtn);
        }

        const nextBtn = document.createElement("button");
        nextBtn.textContent = ">";
        nextBtn.disabled = currentPage === totalPages;
        nextBtn.className = "btn btn-sm btn-outline-secondary mx-1";
        nextBtn.onclick = () => { if (currentPage < totalPages) { currentPage++; mostrarVideos(videos, currentPage); } };
        paginadorContainer.appendChild(nextBtn);
    }

    // Formulario modal
    const formCargarVideo = document.getElementById("formCargarVideo");
    formCargarVideo.addEventListener("submit", async (e) => {
        e.preventDefault();
        const formData = new FormData(formCargarVideo);
        const resp = await fetch("AyudaServlet", { method: "POST", body: formData });
        const data = await resp.json();

        alert(data.message);
        cargarVideos(); // refrescar tarjetas
        formCargarVideo.reset();
        const modal = bootstrap.Modal.getInstance(document.getElementById("modalCargarVideo"));
        modal.hide();
    });

    document.getElementById("formBusqueda").addEventListener("submit", (e) => {
    e.preventDefault();

    const searchTerm = searchInput.value.trim().toLowerCase();

    if (!searchTerm) {
        currentPage = 1;
        mostrarVideos(videosAyudaOriginal, currentPage);
        searchInput.value = "";   // ← LIMPIA
        return;
    }

    const resultados = videosAyudaOriginal.filter(video =>
        (video.titulo || video.nombreDefecto).toLowerCase().includes(searchTerm)
    );

    currentPage = 1;
    mostrarVideos(resultados, currentPage);
    searchInput.value = "";       // ← LIMPIA
});

    // Función para eliminar un video (solo administrador)
    window.eliminarVideo = async function(id) {
    if (!confirm("¿Deseas eliminar este video de ayuda?")) return;

    try {
        const resp = await fetch(`AyudaServlet?id=${id}&accion=eliminar`, { method: 'POST' });

        // 1. Verificar si la respuesta fue exitosa (código 200-299)
        if (!resp.ok) {
            // Si el estado es 500, lanzamos un error claro.
            throw new Error(`Fallo del servidor: Código ${resp.status}. Revisa la consola de Tomcat.`);
        }
        
        // 2. Intentar parsear el JSON
        const data = await resp.json();
        
        // 3. Revisar el status reportado dentro del JSON (del Servlet)
        if (data.status === "error") {
             // Esto captura errores de lógica del Servlet (ej. id no encontrado)
            alert("Error al eliminar: " + data.message);
        } else {
            // Éxito
            alert(data.message);
            cargarVideos(); // refrescar tarjetas
        }
        
    } catch (err) {
        // Captura errores de red, el error lanzado arriba, o fallos de parseo de JSON.
        console.error("Error al procesar la eliminación:", err);
        alert("Error crítico al eliminar el video. Causa: " + err.message);
    }
};
    cargarVideos();
});
