<%-- 
    Document   : Ayuda
    Created on : 22/10/2025, 07:46:43 PM
    Author     : Orlanduvalie Tabares Gutierrez 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Ayuda</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="css/ayuda.css">
</head>
<body>
    <!-- ----Header principal---- -->
  <header class="bg-light py-3">
  <div class="container">
    
    <!-- Logo y Texto Ayuda -->
    <div class="row align-items-center mb-2">
      <div class="col-12 d-flex align-items-center justify-content-start flex-wrap gap-2">
        <img src="img/ayuda 1.png" alt="vender" class="img-fluid" style="width: 70px; height: 70px;">
        <h4 class="mb-0">Ayuda</h4>
      </div>
    </div>

    <!-- Nombre empresa + Barra de búsqueda -->
    <div class="row align-items-center">
      
      <div class="col-12 col-md-6 mb-2 mb-md-0">
        <div style="border-radius: 5px; background-color: #0093DD; padding: 0.5rem 1rem; text-align: center;">
          <div class="fw-bold" style="font-size: calc(1rem + 0.5vw); color: #333652;">MAXI-LIMPIEZA</div>
          <div class="fst-italic" style="font-size: 16px; color: #333652;">Calidad que se siente</div>
        </div>
      </div>
      <div class="col-12 col-md-6 d-flex justify-content-start justify-content-md-end">
        <form id="formBusqueda" class="input-group w-100 w-md-auto">
          <input type="text" 
                 id="busquedaAyuda" 
                 name="busqueda"  
                 class="form-control" 
                 placeholder="Buscar video de ayuda"
                 />
          <input type="hidden" name="accion" value="buscar"/>
          <button type="submit" class="btn btn-outline-secondary"><i class="bi bi-search"></i></button>
        </form>
      </div>
    </div>
  </div>
</header>

    <hr class="mt-0"/>

    <!-- ----Contenido principal---- -->
    <main class="container my-4">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3" id="ayudaVideosContainer"></div>
    </main>

    <!-- ----Paginación y botones---- -->
<div class="container mb-1 d-flex flex-wrap justify-content-between align-items-center gap-1">
    <div class="d-flex gap-1 flex-wrap">
        <button class="btn btn-outline-secondary" onclick="location.href='indexServlet'">Inicio</button>
        <button class="btn btn-outline-danger" onclick="location.href='logoutServlet'">Cerrar sesión</button>
    </div>

    <!-- Aquí el JS dibuja los botones de paginación -->
    <div id="paginadorContainer" class="mt-2 mt-md-0 d-flex align-items-center"></div>
</div>

    <!-- ----Footer---- -->
    <footer>
        <div class="container d-flex justify-content-between flex-wrap">
            <small>© 2025</small>
            <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
        </div>
    </footer>
    
    <!-- Modal para cargar video -->
    <div class="modal fade" id="modalCargarVideo" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <form id="formCargarVideo">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Cargar Video</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              <input type="hidden" id="moduloSeleccionado" name="modulo">
    
              <div class="mb-3">
                <label for="tituloVideo" class="form-label">Título del Video</label>
                <input type="text" class="form-control" id="tituloVideo" name="titulo" placeholder="Video sobre ..." required>
              </div>
    
              <div class="mb-3">
                <label for="urlVideo" class="form-label">URL del Video</label>
                <input type="url" class="form-control" id="urlVideo" name="url_video" required>
              </div>
            </div>
            <div class="modal-footer">
              <button type="submit" class="btn btn-outline-primary">Guardar</button>
              <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
            </div>
          </div>
        </form>
      </div>
    </div>

    <%@ include file="temporizador.jsp" %>
    
    <script src="js/bootstrap.bundle.min.js"></script>
    <script>
    const idRol = ${idRol};
    </script>
    <script src="js/ayuda.js"></script>
</body>
</html>

