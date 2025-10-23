<%-- 
    Document   : error404
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
    <title>Error 404 - MAXI-LIMPIEZA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/404.css">
</head>
<body>
    
    <%-- Contenedor principal de la página de error 404 --%>
    <div id="layoutError" class="container">
        <div id="layoutError_content">
            <main>
                <div class="row justify-content-center">
                    <div class="col-12 col-lg-12 text-center mt-4 px-2">
                        <img class="mb-4 img-error img-fluid" 
                             src="${pageContext.request.contextPath}/img/error-404.png" 
                             alt="Error 404" 
                             style="max-width: 400px; margin-bottom: 20px;" />

                        <p class="lead text-wrap">La URL solicitada no se encontró en este servidor.</p>

                        <c:choose>
                            <c:when test="${not empty sessionScope.usuario}">
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
                </div>
            </main>
        </div>
    </div>

    <%-- Footer de la página de error 404 --%>
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
