<%-- 
    Document   : temporizador llevara a logout despues de 10 minutos de inactividad
    Created on : 19/10/2025, 07:10:03 PM
    Author     : ORLANDUVALIE TABARES GUTIERREZ  
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Temporizador</title>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"/>
    </head>
    <body>
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
    </body>
</html>
