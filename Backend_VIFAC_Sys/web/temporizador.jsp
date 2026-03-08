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

<%-- Temporizador con diseño mejorado --%>
<div class="modal fade" id="modalSesionExpirada" tabindex="-1" 
     aria-labelledby="modalSesionExpiradaLabel" aria-hidden="true" 
     data-bs-backdrop="static" data-bs-keyboard="false">
    
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            
            <%-- Cabecera con degradado y sombra --%>
            <div class="modal-header bg-primary text-white border-0 py-3 shadow-sm">
                <h5 class="modal-title d-flex align-items-center gap-2 fw-bold" id="modalSesionExpiradaLabel">
                    <i class="bi bi-shield-lock-fill fs-4"></i>
                    SEGURIDAD DEL SISTEMA
                </h5>
            </div>

            <div class="modal-body text-center p-4">
                <%-- Icono grande de advertencia --%>
                <div class="mb-4">
                    <div class="display-1 text-danger">
                        <i class="bi bi-clock-history animate-pulse"></i>
                    </div>
                </div>

                <h4 class="text-dark fw-bold mb-3">Sesión Expirada</h4>
                
                <%-- Texto con mejor jerarquía --%>
                <p class="text-muted fs-5 px-3">
                    Tu sesión en <span class="fw-bold text-primary">MAXI-LIMPIEZA</span> ha finalizado tras 10 minutos de inactividad.
                </p>
                
                <div class="alert alert-warning d-inline-block border-0 rounded-3 px-4 py-2 mt-2">
                    <small class="fw-bold">
                        <i class="bi bi-info-circle me-1"></i> 
                        Tus datos han sido protegidos automáticamente.
                    </small>
                </div>
            </div>

            <%-- Botón con estilo moderno --%>
            <div class="modal-footer border-0 pb-4 justify-content-center">
                <a href="logoutServlet" class="btn btn-primary btn-lg px-5 rounded-pill shadow fw-bold">
                    <i class="bi bi-box-arrow-in-right me-2"></i>REINICIAR SESIÓN
                </a>
            </div>
        </div>
    </div>
</div>

<script src="js/temporizador.js"></script>