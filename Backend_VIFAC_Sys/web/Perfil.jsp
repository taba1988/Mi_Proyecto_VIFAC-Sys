<%-- 
    Document   : Perfil
    Created on : 26/10/2025, 10:54:04 AM
    Author     : ORLANDUVALIE TABARES GUTIERREZ
    Nota      : Permite actualizar teléfono y dirección del usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Perfil de Usuario</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/perfil.css" />
</head>
<body>

<header class="bg-light py-3">
    <div class="container d-flex flex-column gap-3">

        <%-- Encabezado principal --%>
        <div class="d-flex align-items-center justify-content-between">
            <div class="d-flex align-items-center gap-2">
                <i class="bi bi-person-vcard-fill fs-1 text-info"></i>
                <h4 class="mb-0">Perfil de Usuario</h4>
            </div>
            <div class="d-lg-none">
                <button class="btn p-0 border-0 bg-transparent ms-3" type="button" 
                        data-bs-toggle="offcanvas" data-bs-target="#offcanvasProfile" 
                        aria-controls="offcanvasProfile">
                    <i class="bi bi-list fs-4"></i>
                </button>
            </div>
        </div>

        <%-- Bloque azul con info adicional --%>
        <div class="d-flex align-items-center justify-content-between">
            <div class="bloque-azul flex-grow-1 px-4 py-4 d-flex justify-content-between align-items-center">
                <div class="bloque-azul-texto fw-bold">MAXI-LIMPIEZA</div>
                <div class="bloque-azul-slogan fst-italic">Calidad que se siente</div>
            </div>
        </div>

    </div>
</header>

<hr />

<main class="container profile-container">
    <div class="profile-card">

        <%-- Imagen de perfil y datos básicos --%>
        <div class="profile-image-section d-none d-lg-flex flex-column align-items-center">
            <div class="rounded-circle border border-primary mb-2 overflow-hidden" style="width: 80px; height: 100px;">
                <img 
                    src="${pageContext.request.contextPath}/ImagenPerfilServlet?nombreArchivo=${usuarioLogeado.fotoPerfil != null && !usuarioLogeado.fotoPerfil.isEmpty() ? usuarioLogeado.fotoPerfil : 'avatarperfil.png'}" 
                    alt="Foto de usuario ${usuarioLogeado.nombre}" 
                    class="w-100 h-100 object-fit-cover"
                />
            </div>
            <div class="profile-name mt-3 text-uppercase">${nombre}</div>
            <div class="profile-role text-uppercase">${cargo}</div>
            <div class="profile-ID">${documento}</div>
        </div>

        <%-- Detalles del perfil --%>
        <div class="profile-details-section w-100 flex-grow-1">
            <div class="profile-details-header bg-light">DATOS DE PERFIL</div>
            
            <form id="perfilForm" method="POST" action="${pageContext.request.contextPath}/PerfilServlet" onsubmit="return false;">

                <input type="hidden" name="accion" value="actualizarPerfil" />
                
                <ul class="profile-details-list list-unstyled">

                    <%-- Nombres y Apellidos --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Nombres y Apellidos:</strong> <span class="text-uppercase">${nombre}</span>
                    </li>

                    <%-- Empresa --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Empresa:</strong> ${empresa}
                    </li>

                    <%-- Dependencia --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Dependencia:</strong> <span class="text-uppercase">${dependencia}</span>
                    </li>

                    <%-- Celular editable --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Celular:</strong> 
                        <input type="tel" class="form-control form-control-sm w-50" id="telefono" name="telefono" value="${telefono}">
                    </li>

                    <%-- Situación Laboral --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Situación Laboral:</strong> <span class="text-uppercase">${situacionLaboral}</span>
                    </li>

                    <%-- Identificación --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Identificación:</strong> 
                        <input type="text" class="form-control form-control-sm w-50" value="${documento}" name="documento" readonly>
                    </li>

                    <%-- Usuario --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Usuario:</strong> ${usuarioAutenticado}
                    </li>

                    <%-- Nota --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Nota:</strong> ${notaSistema}
                    </li>

                    <%-- Cargo --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Cargo:</strong> <span class="text-uppercase">${cargo}</span>
                    </li>

                    <%-- Dirección editable --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Dirección:</strong> 
                        <textarea class="form-control form-control-sm w-50" id="direccion" name="direccion">${direccion}</textarea>
                    </li>

                    <%-- Correo Electrónico --%>
                    <li class="d-flex justify-content-between align-items-center">
                        <strong>Correo Electrónico:</strong> 
                        <input type="email" class="form-control form-control-sm w-50" value="${email}" name="email" readonly>
                    </li>

                </ul>

                <%-- Botones de acción --%>
                <div class="d-flex justify-content-between align-items-center mt-3">          
                    <div class="d-flex gap-4">
                        <button class="btn btn-outline-secondary" type="button" onclick="location.href='${pageContext.request.contextPath}/indexServlet'">Inicio</button>
                        <button class="btn btn-outline-danger" type="button" onclick="location.href='${pageContext.request.contextPath}/logoutServlet'">Cerrar</button>
                    </div>
                    <%-- Botón abre el modal de confirmación --%>
                    <button type="button" class="btn btn-outline-primary" onclick="confirmarGuardar()">Guardar Cambios</button>
                </div>

            </form>
        </div>

    </div>
</main>

<%-- Offcanvas para móviles --%>
<div class="offcanvas offcanvas-start d-lg-none" tabindex="-1" id="offcanvasProfile" aria-labelledby="offcanvasProfileLabel">
    <div class="offcanvas-header">
        <h5 class="offcanvas-title" id="offcanvasProfileLabel">Perfil</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
    </div>
    <div class="offcanvas-body text-center">
        <div class="d-flex justify-content-center align-items-center">
            <div class="rounded-circle border border-primary mb-2 overflow-hidden" style="width: 80px; height: 100px;">
                <img 
                    src="${pageContext.request.contextPath}/ImagenPerfilServlet?nombreArchivo=${usuarioLogeado.fotoPerfil != null && !usuarioLogeado.fotoPerfil.isEmpty() ? usuarioLogeado.fotoPerfil : 'avatarperfil.png'}" 
                    alt="Foto de usuario ${usuarioLogeado.nombre}" 
                    class="w-100 h-100 object-fit-cover"
                />
            </div>
        </div>

        <div class="profile-name mt-3 text-uppercase">${nombre}</div>
        <div class="profile-role text-uppercase">${cargo}</div>
        <div class="profile-ID">${documento}</div>
    </div>
    <div class="d-flex justify-content-center align-items-center mb-5 gap-4"> 
        <button class="btn btn-outline-secondary" type="button" onclick="location.href='${pageContext.request.contextPath}/indexServlet'">Inicio</button>
        <button class="btn btn-outline-danger" type="button" onclick="location.href='${pageContext.request.contextPath}/logoutServlet'">Cerrar</button>
    </div>
</div>

<footer class="bg-light py-3 mt-4">
    <div class="container d-flex justify-content-between flex-wrap">
        <small>© 2025</small>
        <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
    </div>
</footer>

<%-- Modal confirmar guardar cambios --%>
<div class="modal fade" id="confirmarGuardarModal" tabindex="-1" aria-labelledby="confirmarGuardarModalLabel"
     data-bs-backdrop="static" data-bs-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title" id="confirmarGuardarModalLabel">Confirmar Guardar Cambios del Perfil</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                ¿Está seguro de que desea guardar los cambios realizados en su perfil?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                <%-- Botón llama a la función JS que hace fetch al servlet --%>
                <button type="button" class="btn btn-outline-primary" onclick="guardarCambios()">Aceptar</button>
            </div>
        </div>
    </div>
</div>
                
<div class="modal fade" id="modalMensaje" tabindex="-1" aria-labelledby="modalMensajeLabel" 
      data-bs-backdrop="static" data-bs-keyboard="false" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header bg-info text-white">
        <h5 class="modal-title" id="modalMensajeLabel">Mensaje</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body" id="modalMensajeTexto"></div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline-primary" data-bs-dismiss="modal">Aceptar</button>
      </div>
    </div>
  </div>
</div>

<%-- Include temporizador --%>
<%@ include file="temporizador.jsp" %>

<!-- Scripts -->
<script src="js/bootstrap.bundle.min.js"></script>
<script src="js/perfil.js"></script>
</body>
</html>