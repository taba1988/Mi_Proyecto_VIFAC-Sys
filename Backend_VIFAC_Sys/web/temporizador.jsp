<%-- 
    Document   : temporizador llevara a logout despues de 10 minutos de inactividad
    Created on : 19/10/2025, 07:10:03 PM
    Author     : ORLANDUVALIE TABARES GUTIERREZ  
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- 
     IMPORTANTE: Se eliminan etiquetas <html>, <head> y <body> para evitar 
     conflictos de duplicidad al usar <%@ include %> en otros JSP.
--%>

<%-- Modal de sesión expirada --%>
<div class="modal fade" id="modalSesionExpirada" tabindex="-1" 
     aria-labelledby="modalSesionExpiradaLabel" aria-hidden="true" 
     data-bs-backdrop="static" data-bs-keyboard="false">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <%-- Cabecera del modal --%>
            <div class="modal-header bg-primary text-white">
                <h3 class="modal-title" id="modalSesionExpiradaLabel">
                    ⛔ Sesión expirada
                </h3>
            </div>

            <%-- Cuerpo del modal --%>
            <div class="modal-body">
                <p>Tu sesión ha expirado por inactividad.</p>
                <p>Para continuar debes iniciar sesión nuevamente.</p>
            </div>

            <%-- Pie del modal con botón de acción --%>
            <div class="modal-footer">
                <a href="logoutServlet" class="btn btn-outline-primary">Aceptar</a>
            </div>
        </div>
    </div>
</div>

<script src="js/temporizador.js"></script>