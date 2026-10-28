<%--
    Archivo: Loading.jsp
    Autor: Orlanduvalie Tabares Gutierrez
    Descripción:
    Fragmento JSP reutilizable que muestra un overlay de carga animado
    con burbujas, coherente con el diseño de Bubbles.jsp.
    Se incluye en otras páginas mediante:
    <%@ include file="Loading.jsp" %>
--%>

<link rel="stylesheet" href="css/loading.css">

<div id="loadingOverlay">
    <div class="loading-bub-wrap">
        <div class="loading-orbit ob1"></div>
        <div class="loading-orbit ob2"></div>
        <div class="loading-orbit ob3"></div>
        <div class="loading-main-bub"></div>
    </div>
    <div class="loading-label" id="loadingTexto">Cargando<span class="loading-dots"></span></div>
</div>

<script src="js/loading.js"></script>
