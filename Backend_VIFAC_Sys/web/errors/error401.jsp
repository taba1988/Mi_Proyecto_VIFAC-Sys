<%-- 
    Document   : error401
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
    <title>Error de Acceso - MAXI-LIMPIEZA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/401.css">
</head>
<body>
    <%-- Contenedor principal de la página de error 401 --%>
    <div class="error-container container text-center">
        <h1 class="error-code">401</h1>
        <p class="error-message">Acceso No Autorizado</p>
        <p class="mb-3">Lo sentimos, no tiene los permisos necesarios para acceder a este recurso.</p>
        <c:choose>
            <c:when test="${not empty sessionScope.idUsuario}">
                <a href="${pageContext.request.contextPath}/indexServlet" class="return-button">
                    <i class="bi bi-arrow-left me-1"></i> Atras
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/loginServlet" class="return-button">
                    <i class="bi bi-arrow-left me-1"></i> Atras
                </a>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Footer de la página de error 401 --%>
    <div id="layoutError_footer">
        <footer class="bg-light py-3 mt-4">
            <div class="container-fluid d-flex justify-content-between flex-wrap">
                <small>© 2025</small>
                <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
            </div>
        </footer>
    </div>

    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
