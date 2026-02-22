<%-- 
    Document   : Contabilidad
    Created on : 21/02/2026, 4:33:10 p. m.
    Author     : Orlanduvalie Tabares Gutierrez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Contabilidad</title>

    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/contabilidad.css">
</head>
<body>

       <%-- 
        ==========================================================
        MODAL TEMPORAL – MÓDULO EN DESARROLLO
        ----------------------------------------------------------
        Este modal se utiliza para notificar que los módulos 
        de Gestión Contable (Contabilidad y Movimientos) aún 
        no están implementados en backend.
    
        IMPORTANTE:
        Cuando Contabilidad y Movimientos estén completamente 
        desarrollados, eliminar el uso del onclick que invoca 
        este modal y redirigir directamente a sus respectivos 
        Servlets.
        ==========================================================
    --%>
   <div class="modal fade" id="modalDesarrollo"
     data-bs-backdrop="static"
     data-bs-keyboard="false"
     tabindex="-1">

    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0" style="border-radius: 15px;">
            <div class="modal-header bg-primary text-white" style="border-radius: 15px 15px 0 0;">
                <h5 class="modal-title w-100 text-center fw-bold">
                    <i class="bi bi- cone-striped me-2 animate-pulse"></i>
                    PRÓXIMAMENTE
                </h5>
            </div>
            
            <div class="modal-body text-center py-4">
                <div class="mb-3">
                    <i class="bi bi-gear-fill text-primary-emphasis" style="font-size: 3rem; opacity: 0.6;"></i>
                </div>
                
                <h4 class="fw-bold text-dark">Módulo en Desarrollo</h4>
                
                <p class="text-secondary px-3">
                    <i class="bi bi-info-circle me-1 text-primary"></i>
                    Estamos trabajando para integrar las funciones de <strong>Gestión Contable</strong>.
                </p>
                
                <div class="alert alert-light border-0 small text-muted mb-0">
                    <i class="bi bi-megaphone me-1"></i>
                    Se notificará a su usuario cuando el acceso esté habilitado para producción.
                </div>
            </div>

            <div class="modal-footer border-0 pb-4 justify-content-center">
                <button type="button" 
                        class="btn btn-outline-primary px-5 fw-bold shadow-sm"
                        style="border-radius: 10px;"
                        onclick="window.location.href='indexServlet'">
                    <i class="bi bi-check2-circle me-2"></i>Aceptar
                </button>
            </div>
        </div>
    </div>
</div>
    
    <%-- 
    Componente de seguridad: Controla el tiempo de inactividad de la sesión 
    y realiza el cierre automático si es necesario. 
    --%>

    <%@ include file="temporizador.jsp" %>

    <!-- Scripts al final como en tu estructura -->
    <script src="js/bootstrap.bundle.min.js"></script>
    <script src="js/contabilidad.js"></script>

</body>
</html>