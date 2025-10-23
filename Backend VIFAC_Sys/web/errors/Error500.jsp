<%-- 
    Document   : error500
    Created on : 22/10/2025
    Author     : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Error 500 - MAXI-LIMPIEZA</title>
    <!-- Desde /webpages/errors a /webpages/css -->
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="../css/500.css">
</head>
<body>
    
    <%-- Contenedor principal de la página de error --%>
    <div id="layoutError">
        <div id="layoutError_content">
            <main>
                <div class="error-container text-center mt-4 px-2">
                    <h1 class="error-code">500</h1>
                    <p class="error-message">Error Interno del Servidor</p>
                    <p class="lead text-wrap">Ha ocurrido un error inesperado en el servidor.</p>
                    <a href="../indexServlet" class="return-link">
                        <i class="bi bi-arrow-left me-1"></i>
                        Volver al Inicio
                    </a>
                </div>
            </main>
        </div>
    </div>

    <%-- Footer de la página de error --%>
    <div id="layoutError_footer" style="background-color: #dc3545; color: white;">
        <footer class="bg-light py-3 mt-auto">
            <div class="container-fluid d-flex justify-content-between flex-wrap" style="color: white;">
                <small>© 2025</small>
                <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
            </div>
        </footer>
    </div>

    <script src="../js/bootstrap.bundle.min.js"></script>
</body>
</html>
