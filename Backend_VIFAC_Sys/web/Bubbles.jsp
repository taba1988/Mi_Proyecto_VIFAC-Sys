<%-- 
    Archivo: Bubbles.jsp
    Autor: Orlanduvalie Tabares Gutierrez
    Descripción:
    Fragmento JSP reutilizable que agrega un fondo animado con burbujas
    para páginas del sistema (login, restablecer contraseña, etc.).
    Este archivo se incluye en otras páginas mediante:

    <%@ include file="Bubbles.jsp" %>
--%>

<%-- Canvas donde se renderiza la animación de burbujas --%>
<canvas id="bubbles"></canvas>

<%-- Hoja de estilos encargada del diseño del fondo y posicionamiento del canvas --%>
<link rel="stylesheet" href="css/Bubbles.css">

<%-- Script JavaScript que genera y anima las burbujas dinámicas --%>
<script src="js/Bubbles.js"></script>